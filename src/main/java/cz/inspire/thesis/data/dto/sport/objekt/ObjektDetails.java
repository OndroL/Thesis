package cz.inspire.thesis.data.dto.sport.objekt;

import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ObjektDetails {
    private String id;
    private int kapacita;
    private int casovaJednotka;
    private int typRezervace;
    private boolean primyVstup;
    private Integer minDelkaRezervace;
    private Integer maxDelkaRezervace;
    private Integer volnoPredRezervaci;
    private Integer volnoPoRezervaci;
    private Integer zarovnatZacatekRezervace;
    private Integer delkaRezervaceNasobkem;
    private Boolean vicestavovy;
    private Integer stav;
    private Integer[] reservationStart;
    private Integer[] reservationFinish;
    private boolean odcitatProcedury;
    private boolean rezervaceNaTokeny;
    private boolean rucniUzavreniVstupu;
    private boolean upraveniCasuVstupu;
    private boolean pozastavitVstup;
    private boolean showProgress;
    private boolean checkTokensCount;
    private boolean selectInstructor;
    private boolean showInstructorName;
    private boolean showSportName;
    private Integer vytvoreniRezervacePredZacatkem;
    private Integer editaceRezervacePredZacatkem;
    private Integer zruseniRezervacePredZacatkem;
    private Map<String, ObjektLocDetails> localeData;
    private ArealDetails areal;
    private List<SportDetails> sports;
    private List<OvladacObjektuDetails> ovladaceObjektu;
    private List<PodminkaRezervaceDetails> podminkyRezervaci;
    private List<String> nadobjekty;
    private List<String> podobjekty;
    private String googleCalendarId;
    private boolean googleCalendarNotification;
    private int googleCalendarNotificationBefore;
}
