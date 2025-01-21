package cz.inspire.common.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
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