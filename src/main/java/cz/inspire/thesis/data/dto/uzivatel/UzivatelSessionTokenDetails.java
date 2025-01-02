package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UzivatelSessionTokenDetails {
    private String series;
    private String username;
    private String token;
    private Date lastUsed;
}

