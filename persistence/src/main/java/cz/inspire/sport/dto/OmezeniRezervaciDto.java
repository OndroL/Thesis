package cz.inspire.sport.dto;

import cz.inspire.sport.utils.OtviraciDoba;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OmezeniRezervaciDto implements Serializable {
    private String objektId;
    private OtviraciDoba omezeni;
}
