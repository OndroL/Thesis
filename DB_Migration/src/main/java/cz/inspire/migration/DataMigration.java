package cz.inspire.migration;

import cz.inspire.migration.config.AppConfig;
import cz.inspire.migration.config.ColumnConfig;
import cz.inspire.migration.config.TableConfig;
import cz.inspire.migration.migrators.MigratorFactory;
import cz.inspire.migration.utils.MigrationTarget;
import cz.inspire.migration.utils.Migrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DataMigration {
    private static final Logger logger = LoggerFactory.getLogger(DataMigration.class);
    private final AppConfig config;

    public DataMigration(AppConfig config) {
        this.config = config;
    }

    public void migrateAllData() {
        String url = String.format("jdbc:postgresql://%s:%d/%s",
                config.getDbHost(),
                config.getDbPort(),
                config.getDbName());

        String user = config.getDbUser();
        String password = config.getDbPassword();

        Connection readConn = null;
        Connection writeConn = null;

        try {
            // Establish read connection
            readConn = DriverManager.getConnection(url, user, password);
            readConn.setAutoCommit(false); // Manage transactions manually

            // Establish write connection
            writeConn = DriverManager.getConnection(url, user, password);
            writeConn.setAutoCommit(false); // Manage transactions manually

            List<TableConfig> tables = config.getTables();
            if (tables == null || tables.isEmpty()) {
                logger.warn("No tables configured for migration.");
                return;
            }

            for (TableConfig table : tables) {
                String tableName = table.getName();
                String primaryKey = table.getPrimaryKey();
                logger.info("Starting migration for table '{}'.", tableName);

                if (primaryKey == null || primaryKey.isEmpty()) {
                    logger.error("Primary key not defined for table '{}'. Skipping.", tableName);
                    continue; // Skip to next table
                }

                List<ColumnConfig> columns = table.getColumns();
                if (columns == null || columns.isEmpty()) {
                    logger.warn("No columns configured for table '{}'. Skipping.", tableName);
                    continue; // Skip to next table
                }

                for (ColumnConfig column : columns) {
                    String columnName = column.getName();
                    String targetStr = column.getTarget();
                    MigrationTarget target;

                    try {
                        target = MigrationTarget.fromString(targetStr);
                    } catch (IllegalArgumentException e) {
                        logger.error("Unknown migration target '{}' for table '{}', column '{}'. Skipping.", targetStr, tableName, columnName);
                        continue; // Skip to next column
                    }

                    Migrator migrator = MigratorFactory.getMigrator(target);
                    try {
                        migrator.migrate(readConn, writeConn, tableName, primaryKey, column, config.getMigrationBatchSize());
                    } catch (SQLException e) {
                        logger.error("Migration failed for table '{}', column '{}'. Continuing with next.", tableName, columnName, e);
                    }
                }

                logger.info("Completed migration for table '{}'.", tableName);
            }

            logger.info("Data migration completed successfully.");
        } catch (SQLException e) {
            logger.error("Database connection or query failed.", e);
        } catch (Exception e) {
            logger.error("Unexpected error during migration.", e);
        } finally {
            // Ensure transactions are committed or rolled back before closing connections
            if (readConn != null) {
                try {
                    if (!readConn.getAutoCommit()) {
                        readConn.rollback(); // Rollback uncommitted changes if any
                    }
                    readConn.close();
                } catch (SQLException e) {
                    logger.error("Failed to rollback or close read connection.", e);
                }
            }
            if (writeConn != null) {
                try {
                    if (!writeConn.getAutoCommit()) {
                        writeConn.rollback(); // Rollback uncommitted changes if any
                    }
                    writeConn.close();
                } catch (SQLException e) {
                    logger.error("Failed to rollback or close write connection.", e);
                }
            }
        }
    }

    public void cleanUpAfterMigration() {
        String url = String.format("jdbc:postgresql://%s:%d/%s",
                config.getDbHost(),
                config.getDbPort(),
                config.getDbName());

        String user = config.getDbUser();
        String password = config.getDbPassword();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);

            List<TableConfig> tables = config.getTables();
            if (tables == null || tables.isEmpty()) {
                logger.warn("No tables configured for cleanup.");
                return;
            }

            for (TableConfig table : tables) {
                String tableName = table.getName();
                List<ColumnConfig> columns = table.getColumns();
                if (columns == null || columns.isEmpty()) {
                    logger.warn("No columns configured for table '{}'. Skipping cleanup.", tableName);
                    continue;
                }

                for (ColumnConfig column : columns) {
                    String originalColumnName = column.getName();
                    String newColumnName = originalColumnName + "_json";

                    // Drop the original column
                    String dropColumnSQL = String.format("ALTER TABLE %s DROP COLUMN IF EXISTS %s;", tableName, originalColumnName);
                    stmt.addBatch(dropColumnSQL);

                    // Rename the new column back to the original column name
                    String renameColumnSQL = String.format("ALTER TABLE %s RENAME COLUMN %s TO %s;", tableName, newColumnName, originalColumnName);
                    stmt.addBatch(renameColumnSQL);
                }

                // Execute batch for the current table
                int[] results = stmt.executeBatch();
                logger.info("Cleanup executed for table '{}': {} statements executed.", tableName, results.length);
            }

            conn.commit();
            logger.info("Cleanup completed successfully.");
        } catch (SQLException e) {
            logger.error("Failed to clean up after migration.", e);
        }
    }

    public static void main(String[] args) {
        try {
            // Pass both properties and YAML file names
            AppConfig config = new AppConfig("application.properties", "migration-config.yaml");
            DataMigration migration = new DataMigration(config);
            migration.migrateAllData();

            // Delete binary columns and renames newly migrated columns appropriate name
            // Do not run before checking data !!!!
            // migration.cleanUpAfterMigration();
        } catch (IOException e) {
            logger.error("Failed to load configuration.", e);
        }
    }
}