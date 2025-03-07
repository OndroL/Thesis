package cz.inspire.migration.config;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Getter
public class AppConfig {
    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;
    private final int migrationBatchSize;
    private final List<TableConfig> tables;

    public AppConfig(String propertiesFileName, String yamlFileName) throws IOException {
        // Load application.properties
        Properties properties = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                throw new IOException("Configuration file " + propertiesFileName + " not found.");
            }
            properties.load(input);
        }

        this.dbHost = properties.getProperty("db.host");
        this.dbPort = Integer.parseInt(properties.getProperty("db.port"));
        this.dbName = properties.getProperty("db.name");
        this.dbUser = properties.getProperty("db.user");
        this.dbPassword = properties.getProperty("db.password");
        this.migrationBatchSize = Integer.parseInt(properties.getProperty("migration.batch.size"));

        // Load migration-config.yaml
        LoaderOptions options = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(MigrationConfig.class, options));
        try (InputStream yamlInput = AppConfig.class.getClassLoader().getResourceAsStream(yamlFileName)) {
            if (yamlInput == null) {
                throw new IOException("Migration configuration file " + yamlFileName + " not found.");
            }
            MigrationConfig migrationConfig = yaml.load(yamlInput);
            this.tables = migrationConfig.getTables();
        }
    }
}