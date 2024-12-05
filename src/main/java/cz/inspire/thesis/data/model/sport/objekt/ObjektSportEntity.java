package cz.inspire.thesis.data.model.sport.objekt;

import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "objekt_sport")
public class ObjektSportEntity {
    @Id
    private String id;
    @Column(nullable = false)
    private int index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", referencedColumnName = "id", nullable = false)
    private SportEntity sport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objekt_id", referencedColumnName = "id", nullable = false)
    private ObjektEntity objekt;
}