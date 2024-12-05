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
@Table(name = "objekt_loc")
public class ObjektLocEntity {
    @Id
    private String id;
    @Column
    private String jazyk;
    @Column
    private String nazev;
    @Column
    private String popis;
    @Column
    private String zkracenyNazev;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objekt")
    private ObjektEntity objekt;
}
