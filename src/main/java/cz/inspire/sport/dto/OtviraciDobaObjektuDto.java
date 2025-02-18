package cz.inspire.sport.dto;

import cz.inspire.sport.utils.OtviraciDoba;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class OtviraciDobaObjektuDto implements Serializable {
    private String objektId;
    private Date platnostOd;
    private OtviraciDoba otviraciDoba;
}