package cz.inspire.migration.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnConfig {
    private String name;
    private String target; // "jsonb" or "filesystem"
    private String fileExtension; // Optional, used for filesystem
    private String targetConfig; // JSON template with placeholders
}
