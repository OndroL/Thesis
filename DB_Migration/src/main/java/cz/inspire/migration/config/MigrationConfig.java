package cz.inspire.migration.config;

import java.util.List;

public class MigrationConfig {
    private List<TableConfig> tables;

    // Public no-arg constructor
    public MigrationConfig() {}

    // Getter and Setter
    public List<TableConfig> getTables() {
        return tables;
    }

    public void setTables(List<TableConfig> tables) {
        this.tables = tables;
    }
}
