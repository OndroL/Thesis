package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SportKategorieDto implements Serializable {
    private String id;
    private String multisportFacilityId;
    private String multisportServiceUUID;
    private String nadrazenaKategorieId;
    private Map<String, SportKategorieLocDto> localeData;
}
