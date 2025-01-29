package cz.inspire.migration.utils;

public enum MigrationTarget {
    JSONB,
    FILESYSTEM;

    public static MigrationTarget fromString(String target) {
        return switch (target.toLowerCase()) {
            case "jsonb" -> JSONB;
            case "filesystem" -> FILESYSTEM;
            default -> throw new IllegalArgumentException("Unknown migration target: " + target);
        };
    }
}