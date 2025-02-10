package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

