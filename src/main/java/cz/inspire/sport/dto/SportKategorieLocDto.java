package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SportKategorieLocDto implements Serializable {
    private String id;
    private String jazyk;
    private String nazev;
    private String popis;
}