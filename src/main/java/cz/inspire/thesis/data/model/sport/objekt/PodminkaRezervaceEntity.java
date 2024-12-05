package cz.inspire.thesis.data.model.sport.objekt;

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
    @JoinColumn(name = "objektid", referencedColumnName = "id")
    private ObjektEntity objekt;
}
