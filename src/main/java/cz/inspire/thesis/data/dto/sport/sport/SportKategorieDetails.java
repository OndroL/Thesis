package cz.inspire.thesis.data.dto.sport.sport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SportKategorieDetails {
    private String id;
    private String multisportFacilityId;
    private String multisportServiceUUID;
    private String nadrazenaKategorieId;
    private Map<String, SportKategorieLocDetails> localeData;
}
