package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArealDto {
    private String id;
    private Integer pocetNavazujucichRez;
    private Map<String, ArealLocDto> localeData;
    private String nadrazenyArealId;
}
