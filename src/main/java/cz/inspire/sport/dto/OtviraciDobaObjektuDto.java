package cz.inspire.sport.dto;

import cz.inspire.sport.utils.OtviraciDoba;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtviraciDobaObjektuDto {
    private String objektId;
    private Date platnostOd;
    private OtviraciDoba otviraciDoba;
}