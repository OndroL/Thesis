package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "podminka_rezervace")
public class PodminkaRezervaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column
    private long priorita;

    @Column
    private String objektRezervaceId;

    @Column
    private Boolean objektRezervaceObsazen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "objektId", referencedColumnName = "id")
    private ObjektEntity objekt;
}
