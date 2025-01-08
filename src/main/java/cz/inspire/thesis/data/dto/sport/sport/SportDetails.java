package cz.inspire.thesis.data.dto.sport.sport;

import cz.inspire.thesis.data.utils.SazbaStorna;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SportDetails {
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
    private Map<String, SportLocDetails> localeData;
    private SportKategorieDetails sportKategorie;
    private Set<InstructorDetails> instructors;
    private String nadrazenySportId;
    private String navazujiciSportId;
    private String activityId;
}
