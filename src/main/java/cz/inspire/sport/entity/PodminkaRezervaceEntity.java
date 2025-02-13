package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "podminka_rezervace")
public class PodminkaRezervaceEntity {
    @Id
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
