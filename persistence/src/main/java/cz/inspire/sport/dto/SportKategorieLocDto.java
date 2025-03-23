package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SportKategorieLocDto implements Serializable {
    private String id;
    private String jazyk;
    private String nazev;
    private String popis;
}