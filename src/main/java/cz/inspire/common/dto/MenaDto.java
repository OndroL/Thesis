package cz.inspire.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class MenaDto implements Serializable {
    private String id;
    private String kod;
    private String vycetka;
    private int kodNum;
    private int zaokrouhleniHotovost;
    private int zaokrouhleniKarta;
    private List<BigDecimal> vycetkaList;
}