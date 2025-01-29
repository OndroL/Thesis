package cz.inspire.migration.migrators;

import cz.inspire.migration.utils.MigrationTarget;
import cz.inspire.migration.utils.Migrator;

public class MigratorFactory {
    public static Migrator getMigrator(MigrationTarget target) {
        return switch (target) {
            case JSONB -> new JsonbMigrator();
            case FILESYSTEM -> new FilesystemMigrator();
            default -> throw new IllegalArgumentException("Unknown target type: " + target);
        };
    }
}