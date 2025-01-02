package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UzivatelDetails {
    private String login;
    private String heslo;
    private String celeJmeno;
    private boolean aktivni;
    private String aktivacniToken;
    private String authKey;
    private Map<String, Object> nastaveni;
    private List<PushKeyDetails> pushKeys;
}
