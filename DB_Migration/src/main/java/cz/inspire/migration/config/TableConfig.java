package cz.inspire.migration.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableConfig {
    private String name;
    private String primaryKey;
    private List<ColumnConfig> columns;
}