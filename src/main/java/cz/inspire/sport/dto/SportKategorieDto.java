package cz.inspire.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class SportKategorieDto implements Serializable {
    private String id;
    private String multiSportFacilityId;
    private String multiSportServiceUUID;
    private String nadrazenaKategorieId;
    private Map<String, SportKategorieLocDto> localeData;
}
