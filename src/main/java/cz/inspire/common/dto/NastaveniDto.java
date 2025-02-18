package cz.inspire.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NastaveniDto implements Serializable {
    private String key;
    private Serializable value;
}
