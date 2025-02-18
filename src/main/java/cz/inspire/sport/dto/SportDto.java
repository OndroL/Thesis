package cz.inspire.sport.dto;

import cz.inspire.sport.utils.SazbaStorna;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class SportDto implements Serializable {
    private String id;
    private int typ;
    private String zboziId;
    private String skladId;
    private Integer sazbaJednotek;
    private Boolean sazbaNaOsobu;
    private Integer sazbaNaCas;
    private Boolean uctovatZalohu;
    private int podSportyCount;
    private List<SazbaStorna> sazbyStorna;
    private Integer minDelkaRezervace;
    private Integer maxDelkaRezervace;
    private Boolean objednavkaZaplniObjekt;
    private Integer delkaRezervaceNasobkem;
    private Color barvaPopredi;
    private Color barvaPozadi;
    private Boolean zobrazitText;
    private Boolean viditelnyWeb;
    private Integer navRezervaceOffset;
    private Integer delkaHlavniRez;
    private Integer minimalniPocetOsob;
    private Integer minutyPredVyhodnocenimKapacity;
    private Integer maximalniPocetOsobNaZakaznika;
    private Map<String, SportLocDto> localeData;
    private SportKategorieDto sportKategorie;
    private Set<InstructorDto> instructors;
    private String nadrazenySportId;
    private String navazujiciSportId;
    private String activityId;
}
