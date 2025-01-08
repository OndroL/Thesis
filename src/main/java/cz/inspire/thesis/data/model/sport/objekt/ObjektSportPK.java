package cz.inspire.thesis.data.model.sport.objekt;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ObjektSportPK implements Serializable {
    private String id;
    private int index;
}
