package cz.inspire.thesis.data.dto.sport.objekt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ObjektLocDetails {
    private String id;
    private String jazyk;
    private String nazev;
    private String popis;
    private String zkracenyNazev;
}