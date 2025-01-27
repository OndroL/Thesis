package cz.inspire.migration.config;

import java.util.List;


public class TableConfig {
    private String name;
    private String primaryKey;
    private List<ColumnConfig> columns;

    // Public no-arg constructor
    public TableConfig() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ColumnConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnConfig> columns) {
        this.columns = columns;
    }
}