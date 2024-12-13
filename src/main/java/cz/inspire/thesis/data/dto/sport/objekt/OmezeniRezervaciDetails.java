package cz.inspire.thesis.data.dto.sport.objekt;

import cz.inspire.thesis.data.utils.OtviraciDoba;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OmezeniRezervaciDetails {
    private String objektId;
    private OtviraciDoba omezeni;
}
