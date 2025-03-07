package cz.inspire.migration.migrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inspire.migration.config.ColumnConfig;
import cz.inspire.migration.utils.FileAttributes;
import cz.inspire.migration.utils.JBossDeserializer;
import cz.inspire.migration.utils.Migrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class FilesystemMigrator implements Migrator {
    private static final Logger logger = LoggerFactory.getLogger(FilesystemMigrator.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    // Base directory where attachments will be stored
    private static final String BASE_DIR = "FILE_SYSTEM";

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

        try (PreparedStatement selectStmt = readConn.prepareStatement(selectSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
             PreparedStatement updateStmt = writeConn.prepareStatement(updateSQL)) {

            selectStmt.setFetchSize(batchSize);
            logger.debug("Executing SELECT query: {}", selectSQL);
            try (ResultSet rs = selectStmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    String key = rs.getString(primaryKey);
                    byte[] valueBytes = rs.getBytes(originalColumnName);

                    // Handle filesystem migration
                    String finalJsonValue = handleFilesystemMigration(tableName, key, valueBytes, targetConfig, originalColumnName);

                    // Set parameters for UPDATE and add to batch
                    if (finalJsonValue != null) {
                        updateStmt.setObject(1, finalJsonValue, Types.OTHER);
                    } else {
                        updateStmt.setNull(1, Types.OTHER);
                    }
                    updateStmt.setString(2, key);
                    updateStmt.addBatch();

                    count++;

                    // Execute batch when batch size is reached
                    if (count % batchSize == 0) {
                        executeBatch(updateStmt, writeConn, count, tableName, newColumnName);
                    }
                }

                // Execute remaining batch
                if (count % batchSize != 0) {
                    executeBatch(updateStmt, writeConn, count, tableName, newColumnName);
                }

                logger.info("Completed Filesystem migration for table '{}', column '{}'. Total records processed: {}", tableName, originalColumnName, count);
            } finally {
                selectStmt.close(); // Ensure the statement is closed
            }
        }
    }

    /**
     * Handles migration for columns with target type 'filesystem'.
     */
    private String handleFilesystemMigration(String tableName, String key, byte[] valueBytes, String targetConfig, String originalColumnName) {
        try {
            // Deserialize the stored data.
            Object obj = JBossDeserializer.deserialize(valueBytes);

            // For "attributes", we expect a Map.
            if (Objects.equals(originalColumnName, "attributes")) {
                if (!(obj instanceof Map)) {
                    if (obj != null) {
                        logger.info("Table '{}', Key '{}': Expected a Map for attachments, but found {}", tableName, key, obj.getClass().getName());
                    }
                    return "[]"; // Return empty list in JSON format
                }
            }
            // For "photo", we get a raw byte[] (PNG) instead of a Map.
            else if (Objects.equals(originalColumnName, "photo")) {
                if (obj instanceof byte[]) {
                    // Wrap the raw PNG bytes into a Map for uniform processing.
                    Map<String, byte[]> photoMap = new HashMap<>();
                    photoMap.put(null, (byte[]) obj);
                    obj = photoMap;
                } else if (obj == null) {
                    return "{}"; // Return an empty JSON object as fallback
                }
            }
            // Fallback: if no recognized type, return empty.
            else {
                if(obj == null) {
                    return "[]";
                }
            }

            // Now process the data as a Map<String, byte[]>
            Map<String, byte[]> attachmentsMap = (Map<String, byte[]>) obj;
            List<FileAttributes> attachmentsList = new ArrayList<>();

            for (Map.Entry<String, byte[]> entry : attachmentsMap.entrySet()) {
                String fileName = entry.getKey();
                byte[] fileData = entry.getValue();

                // Generate a unique file path
                String uniqueId = UUID.randomUUID().toString();
                String baseDirPath = BASE_DIR + '/' + targetConfig + '/';
                String filePath = baseDirPath + uniqueId;

                // Ensure the base directory exists
                File baseDir = new File(baseDirPath);
                if (!baseDir.exists() && !baseDir.mkdirs()) {
                    logger.error("Failed to create directories for path '{}'.", baseDirPath);
                    continue; // Skip this attachment
                }

                // Write the file to the filesystem
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(fileData);
                    logger.debug("Table '{}', Key '{}': Stored attachment '{}' at '{}'.", tableName, key, fileName, filePath);
                } catch (IOException e) {
                    logger.error("Table '{}', Key '{}': Failed to write file '{}' to '{}'. Skipping this attachment.", tableName, key, fileName, filePath, e);
                    continue; // Skip this attachment
                }

                // Add file attributes to the list
                attachmentsList.add(new FileAttributes(fileName, filePath));
            }

            // Return JSON: if handling photo, return a single object; otherwise return the list.
            String attachmentsJson;
            if ("photo".equals(originalColumnName)) {
                if (!attachmentsList.isEmpty()) {
                    attachmentsJson = mapper.writeValueAsString(attachmentsList.get(0));
                    if (attachmentsList.size() > 1) {
                        logger.error("Table '{}', Key '{}': Photo has more than 1 object!", tableName, key);
                    }
                } else {
                    attachmentsJson = "{}";
                }
            } else {
                attachmentsJson = mapper.writeValueAsString(attachmentsList);
            }

            logger.debug("Table '{}', Key '{}': {}_json: {}", tableName, key, originalColumnName, attachmentsJson);
            return attachmentsJson;
        } catch (Exception e) {
            logger.error("Table '{}', Key '{}': Failed to process files.", tableName, key, e);
            return "[]"; // Fallback to empty array
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
