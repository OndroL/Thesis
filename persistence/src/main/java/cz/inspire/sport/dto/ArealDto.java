package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArealDto implements Serializable {
    private String id;
    private Integer pocetNavazujucichRez;
    private Map<String, ArealLocDto> localeData = new HashMap<>();
    private String nadrazenyArealId;
}
