package cz.inspire.migration.migrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inspire.migration.config.ColumnConfig;
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
    private static final String ATTACHMENTS_BASE_DIR = "FILE_SYSTEM/attachments/";

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

        try (PreparedStatement selectStmt = readConn.prepareStatement(selectSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
             PreparedStatement updateStmt = writeConn.prepareStatement(updateSQL)) {

            selectStmt.setFetchSize(batchSize);
            logger.debug("Executing SELECT query: {}", selectSQL);
            try (ResultSet rs = selectStmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    String key = rs.getString(primaryKey);
                    byte[] valueBytes = rs.getBytes(originalColumnName);

                    String finalJsonValue = null;

                    // Handle filesystem migration
                    finalJsonValue = handleFilesystemMigration(tableName, key, valueBytes, targetConfig);

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
    private String handleFilesystemMigration(String tableName, String key, byte[] valueBytes, String targetConfig) {
        try {
            // Deserialize the Map from bytea
            Object obj = JBossDeserializer.deserialize(valueBytes);
            if (!(obj instanceof Map)) {
                logger.info("Table '{}', Key '{}': Expected a Map for attachments, but found {}", tableName, key, obj != null ? obj.getClass().getName() : "null");
                return "[]";
            }

            Map<String, byte[]> attachmentsMap = (Map<String, byte[]>) obj; // Adjust type if necessary

            List<Map<String, String>> attachmentsList = new ArrayList<>();

            for (Map.Entry<String, byte[]> entry : attachmentsMap.entrySet()) {
                String fileName = entry.getKey();
                byte[] fileData = entry.getValue();

                // Generate unique identifier for filePath
                String uniqueId = UUID.randomUUID().toString();
                String filePath = ATTACHMENTS_BASE_DIR + uniqueId + "_" + fileName;

                // Ensure the base directory exists
                File baseDir = new File(ATTACHMENTS_BASE_DIR);
                if (!baseDir.exists()) {
                    boolean dirsCreated = baseDir.mkdirs();
                    if (!dirsCreated) {
                        logger.error("Failed to create directories for path '{}'.", ATTACHMENTS_BASE_DIR);
                        continue; // Skip this attachment
                    }
                }

                // Write file to filesystem
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(fileData);
                    logger.debug("Table '{}', Key '{}': Stored attachment '{}' at '{}'.", tableName, key, fileName, filePath);
                } catch (IOException e) {
                    logger.error("Table '{}', Key '{}': Failed to write file '{}' to '{}'. Skipping this attachment.", tableName, key, fileName, filePath, e);
                    continue; // Skip this attachment
                }

                // Add to attachments list
                Map<String, String> attachmentEntry = new HashMap<>();
                attachmentEntry.put("FileName", fileName);
                attachmentEntry.put("FilePath", filePath);
                attachmentsList.add(attachmentEntry);
            }

            // Convert attachments list to JSON
            String attachmentsJson = mapper.writeValueAsString(attachmentsList);
            logger.debug("Table '{}', Key '{}': attachments_json: {}", tableName, key, attachmentsJson);

            return attachmentsJson;
        } catch (Exception e) {
            logger.error("Table '{}', Key '{}': Failed to process attachments.", tableName, key, e);
            // Fallback JSON with empty array
            return "[]";
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
