package cz.inspire.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderDto implements Serializable {
    private String id;
    private int field;
    private int location;
}
