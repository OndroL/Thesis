package RepositoryTests;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
@DataSourceDefinition(
        name = "java:jboss/datasources/PostgreSQLDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "Test", // Make sure this matches your database
        user = "postgres",
        password = "postgres"
)
public class TestDataSourceConfig {
    // no code needed, the annotation does the binding
}
