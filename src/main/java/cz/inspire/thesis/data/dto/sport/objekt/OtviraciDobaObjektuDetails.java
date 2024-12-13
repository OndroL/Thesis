package cz.inspire.thesis.data.dto.sport.objekt;

import cz.inspire.thesis.data.utils.OtviraciDoba;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtviraciDobaObjektuDetails {
    private String objektId;
    private Date platnostOd;
    private OtviraciDoba otviraciDoba;
}