package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ObjektDto implements Serializable {
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
    private Map<String, ObjektLocDto> localeData;
    private ArealDto areal;
    private List<ObjektSportDto> objektSport;
    private List<OvladacObjektuDto> ovladaceObjektu;
    private List<PodminkaRezervaceDto> podminkyRezervaci;
    private Set<String> nadobjekty;
    private Set<String> podobjekty;
    private String googleCalendarId;
    private boolean googleCalendarNotification;
    private int googleCalendarNotificationBefore;
}
