package cz.inspire.thesis.data.dto.token;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {
    private String id;
    private String popis;
    private String typTokenuId;
}
