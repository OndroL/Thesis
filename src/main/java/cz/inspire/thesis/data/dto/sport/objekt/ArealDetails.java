package cz.inspire.thesis.data.dto.sport.objekt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArealDetails {
    private String id;
    private Integer pocetNavazujucichRez;
    private Map<String, ArealLocDetails> localeData;
    private String nadrazenyArealId;
}
