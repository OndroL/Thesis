package cz.inspire.common.dto;

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
public class MenaDto {
    private String id;
    private String kod;
    private String vycetka;
    private int kodNum;
    private int zaokrouhleniHotovost;
    private int zaokrouhleniKarta;
    private List<BigDecimal> vycetkaList;
}