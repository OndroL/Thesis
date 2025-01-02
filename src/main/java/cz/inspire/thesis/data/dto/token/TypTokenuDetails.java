package cz.inspire.thesis.data.dto.token;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypTokenuDetails {
    private String id;
    private boolean autoPrirazovatZakaznikum;
    private boolean zobrazovatNazevTokenu;
    private Map<String, TypTokenuLocDetails> localeData;
}