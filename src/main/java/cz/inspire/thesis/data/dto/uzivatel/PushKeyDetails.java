package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushKeyDetails {
    private String id;
    private String key;
    private String uzivatelId;
}
