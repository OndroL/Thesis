package cz.inspire.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class OvladacObjektuDto implements Serializable {
    private String id;
    private String idOvladace;
    private Boolean manual;
    private Boolean automat;
    private int delkaSepnutiPoKonci;
    private int zapnutiPredZacatkem;
    private List<Integer> cislaZapojeniList;
    private String objektId;
}

