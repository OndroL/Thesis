package cz.inspire.migration.config;

public class ColumnConfig {
    private String name;
    private String target; // "jsonb" or "filesystem"
    private String fileExtension; // Optional, used for filesystem
    private String targetConfig; // JSON template with placeholders

    // Public no-arg constructor
    public ColumnConfig() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getTargetConfig() {
        return targetConfig;
    }

    public void setTargetConfig(String targetConfig) {
        this.targetConfig = targetConfig;
    }
}
