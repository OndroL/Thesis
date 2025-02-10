package cz.inspire.sport.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

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
