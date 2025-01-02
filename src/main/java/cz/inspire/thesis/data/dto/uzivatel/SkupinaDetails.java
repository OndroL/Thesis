package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkupinaDetails {
    private String id;
    private String nazev;
    private Map<String, Object> nastaveni;
}
