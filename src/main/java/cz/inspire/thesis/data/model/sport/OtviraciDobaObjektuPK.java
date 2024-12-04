package cz.inspire.thesis.data.model.sport;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class OtviraciDobaObjektuPK implements Serializable {

    private String objektId;
    private Date platnostOd;
}
