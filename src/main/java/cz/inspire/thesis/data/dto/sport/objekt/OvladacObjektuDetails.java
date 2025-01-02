package cz.inspire.thesis.data.dto.sport.objekt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OvladacObjektuDetails {
    private String id;
    private String idOvladace;
    private Boolean manual;
    private Boolean automat;
    private int delkaSepnutiPoKonci;
    private int zapnutiPredZacatkem;
    private List<Integer> cislaZapojeniList;
    private String objektId;
}

