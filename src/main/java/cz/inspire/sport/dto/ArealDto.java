package cz.inspire.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class ArealDto implements Serializable {
    private String id;
    private Integer pocetNavazujucichRez;
    private Map<String, ArealLocDto> localeData;
    private String nadrazenyArealId;
}
