package cz.inspire.migration.migrators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.inspire.migration.config.ColumnConfig;
import cz.inspire.migration.utils.JBossDeserializer;
import cz.inspire.migration.utils.JsonConverter;
import cz.inspire.migration.utils.Migrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;


public class JsonbMigrator implements Migrator {
    private static final Logger logger = LoggerFactory.getLogger(JsonbMigrator.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void migrate(Connection readConn, Connection writeConn, String tableName, String primaryKey,
                        ColumnConfig columnConfig, int batchSize) throws SQLException {
        String originalColumnName = columnConfig.getName();
        String newColumnName = originalColumnName + "_json";
        String targetConfig = columnConfig.getTargetConfig();
        readConn.setAutoCommit(true);
        writeConn.setAutoCommit(false);

        if (targetConfig == null || targetConfig.isEmpty()) {
            logger.error("targetConfig is not defined for table '{}', column '{}'. Skipping.", tableName, originalColumnName);
            return;
        }

        // Alter the table to add new column (original_column_json) if it doesn't exist
        String alterTableSQL = String.format("ALTER TABLE %s ADD COLUMN IF NOT EXISTS %s jsonb;", tableName, newColumnName);
        try (Statement stmt = writeConn.createStatement()) {
            stmt.execute(alterTableSQL);
            logger.info("Added column '{}' to table '{}'.", newColumnName, tableName);
            writeConn.commit();
        } catch (SQLException e) {
            logger.error("Failed to add column '{}' to table '{}'. Skipping migration for this column.", newColumnName, tableName, e);
            writeConn.commit();
            return;
        }

        // Prepare SELECT and UPDATE statements
        String selectSQL = String.format("SELECT %s, %s FROM %s", primaryKey, originalColumnName, tableName);
        String updateSQL = String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, newColumnName, primaryKey);

        try (PreparedStatement selectStmt = readConn.prepareStatement(
                selectSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
             PreparedStatement updateStmt = writeConn.prepareStatement(updateSQL)) {

            selectStmt.setFetchSize(batchSize);
            logger.debug("Executing SELECT query: {}", selectSQL);
            try (ResultSet rs = selectStmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    String key = rs.getString(primaryKey);
                    byte[] valueBytes = rs.getBytes(originalColumnName);

                    String finalJsonValue;

                    // Deserialize and convert to JSON
                    try {
                        Object obj = JBossDeserializer.deserialize(valueBytes);
                        finalJsonValue = buildJson(obj, columnConfig, tableName, originalColumnName);

                        if (finalJsonValue != null) {
                            logger.debug("Constructed JSON for key '{}': {}", key, finalJsonValue);
                        } else {
                            logger.info("Table '{}', Key '{}': JSON value is null.", tableName, key);
                        }
                    } catch (Exception e) {
                        logger.error("Table '{}', Key '{}': Failed to deserialize or convert data.", tableName, key, e);
                        // Fallback JSON (Set to NULL)
                        finalJsonValue = null;
                    }

                    // Set parameters for UPDATE and add to batch
                    if (finalJsonValue != null) {
                        updateStmt.setObject(1, finalJsonValue, Types.OTHER);
                    } else {
                        updateStmt.setNull(1, Types.OTHER);
                    }
                    updateStmt.setString(2, key);
                    updateStmt.addBatch();

                    count++;

                    if (count % batchSize == 0) {
                        executeBatch(updateStmt, writeConn, count, tableName, newColumnName);
                    }
                }

                // Execute remaining batch
                if (count % batchSize != 0) {
                    executeBatch(updateStmt, writeConn, count, tableName, newColumnName);
                }

                logger.info("Completed JSONB migration for table '{}', column '{}'. Total records processed: {}", tableName, originalColumnName, count);
            } finally {
                selectStmt.close(); // Ensure the statement is closed
            }
        }
    }

    /**
     * Executes the batch of updates and handles exceptions.
     */
    private void executeBatch(PreparedStatement updateStmt, Connection writeConn, int count, String tableName, String columnName) {
        try {
            int[] results = updateStmt.executeBatch();
            writeConn.commit();
            logger.info("Executed batch of {} records for table '{}', column '{}'.", results.length, tableName, columnName);
        } catch (SQLException e) {
            logger.error("Failed to execute batch at record count {} for table '{}', column '{}'. Rolling back.", count, tableName, columnName, e);
            try {
                writeConn.rollback();
                logger.info("Transaction rolled back for table '{}', column '{}'.", tableName, columnName);
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction for table '{}', column '{}'.", tableName, columnName, rollbackEx);
            }
        }
    }

    private String buildJson(Object deserializedObj, ColumnConfig columnConfig, String tableName, String originalColumnName) {
        try {
            if (deserializedObj == null) {
                return null;
            }

            String targetConfig = columnConfig.getTargetConfig();

            if ("list".equals(targetConfig)) {
                // For "list" targetConfig, serialize the object as a JSON array
                return mapper.writeValueAsString(deserializedObj);
            } else if ("nastaveni".equals(targetConfig)) {
                // Build JSON for "nastaveni" case
                String jsonValue = JsonConverter.toJson(deserializedObj);
                JsonNode valueNode = mapper.readTree(jsonValue);

                ObjectNode jsonNode = mapper.createObjectNode();
                jsonNode.set("Value", valueNode);
                jsonNode.put("ClassName", deserializedObj.getClass().getSimpleName());

                return mapper.writeValueAsString(jsonNode);
            } else if ("attributes".equals(targetConfig)) {
                Map<String, Object> attributesMap;
                try {
                    // Assuming deserializedObj is already a compatible Map structure
                    if (deserializedObj instanceof Map) {
                        attributesMap = (Map<String, Object>) deserializedObj;
                    } else {
                        // Fallback: Convert the object into a Map using Jackson
                        attributesMap = mapper.convertValue(deserializedObj, Map.class);
                    }
                    // Convert the Map to a JSON string
                    return mapper.writeValueAsString(attributesMap);

                } catch (Exception e) {
                    logger.error("Table '{}': Failed to process 'attributes' data. Setting column to NULL.", tableName, e);
                    return null; // Default to SQL NULL in case of errors
                }
            } else {
                // Default behavior for other cases
                logger.warn("Unsupported targetConfig '{}' for table '{}', column '{}'. Returning basic Jsonb with Value.", targetConfig, tableName, originalColumnName);
                String jsonValue = JsonConverter.toJson(deserializedObj);
                JsonNode valueNode = mapper.readTree(jsonValue);

                ObjectNode jsonNode = mapper.createObjectNode();
                jsonNode.set("Value", valueNode);

                return mapper.writeValueAsString(jsonNode);
            }
        } catch (Exception e) {
            logger.error("Failed to build JSON for table '{}', column '{}'. Returning null.", tableName, originalColumnName, e);
            return null;
        }
    }
}
