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

        // Step 1: Alter the table to add new column (original_column_json) if it doesn't exist
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

        // Step 2: Prepare SELECT and UPDATE statements
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

                    String finalJsonValue = null;

                    // Deserialize and convert to JSON
                    try {
                        Object obj = JBossDeserializer.deserialize(valueBytes);
                        if (obj != null) {
                            String jsonValue = JsonConverter.toJson(obj);
                            logger.debug("Table '{}', Key '{}': JSON Value: {}", tableName, key, jsonValue);

                            // Parse jsonValue back into JsonNode
                            JsonNode valueNode = mapper.readTree(jsonValue);

                            // Construct the final JSON object based on targetConfig
                            ObjectNode jsonNode = mapper.createObjectNode();
                            if ("nastaveni".equals(tableName) && "value".equals(originalColumnName)) {
                                jsonNode.set("Value", valueNode);
                                jsonNode.put("ClassName", obj.getClass().getSimpleName());
                            } else {
                                // For other tables and columns
                                jsonNode.set("Value", valueNode);
                            }

                            finalJsonValue = mapper.writeValueAsString(jsonNode);
                            logger.debug("Constructed JSON for key '{}': {}", key, finalJsonValue);
                        } else {
                            logger.warn("Table '{}', Key '{}': Deserialized object is null. Setting jsonb column to NULL.", tableName, key);
                            // Set finalJsonValue to null to indicate SQL NULL
                            finalJsonValue = null;
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
}
