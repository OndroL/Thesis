package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OvladacObjektuDto implements Serializable {
    private String id;
    private String idOvladace;
    private Boolean manual;
    private Boolean automat;
    private int delkaSepnutiPoKonci;
    private int zapnutiPredZacatkem;
    private List<Integer> cislaZapojeniList = new ArrayList<>();
    private String objektId;
}

