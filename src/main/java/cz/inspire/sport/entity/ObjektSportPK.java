package cz.inspire.sport.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class ObjektSportPK implements Serializable {
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int index;
}
