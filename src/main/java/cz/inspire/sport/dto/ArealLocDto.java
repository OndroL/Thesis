package cz.inspire.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ArealLocDto implements Serializable {
    private String id;
    private String jazyk;
    private String nazev;
    private String popis;
}
