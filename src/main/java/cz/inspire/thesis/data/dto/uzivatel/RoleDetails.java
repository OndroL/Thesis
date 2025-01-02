package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDetails {
    private String id;
    private String nazev;
    private String popis;
}
