package cz.inspire.thesis.data.dto.token;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenConfirmationDetails {
    private String id;
    private Date cas;
    private String zakaznikId;
    private String uzivatelId;
    private String tokenId;
    private boolean confirmation;
}