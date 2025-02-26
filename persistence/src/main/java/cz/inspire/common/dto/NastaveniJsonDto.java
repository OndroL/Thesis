package cz.inspire.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NastaveniJsonDto implements Serializable {
    private String key;
    private String value;
}
