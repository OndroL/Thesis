package cz.inspire.migration.utils;

import cz.inspire.migration.config.ColumnConfig;

import java.sql.Connection;
import java.sql.SQLException;

public interface Migrator {
    /**
     * Migrates a specific column based on its configuration.
     *
     * @param readConn     The database connection for reading data.
     * @param writeConn    The database connection for writing data.
     * @param tableName    The name of the table.
     * @param primaryKey   The primary key column name.
     * @param columnConfig The configuration for the column.
     * @param batchSize    The size of the batch for processing records.
     * @throws SQLException If a database access error occurs.
     */
    void migrate(Connection readConn, Connection writeConn, String tableName, String primaryKey, ColumnConfig columnConfig, int batchSize) throws SQLException;
}
