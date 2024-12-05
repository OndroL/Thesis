package cz.inspire.thesis.data.model.sport.objekt;

import cz.inspire.thesis.data.utils.OtviraciDoba;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "otviraci_doba")
public class OtviraciDobaObjektuEntity {
    @EmbeddedId
    private OtviraciDobaObjektuPK id;

    @MapsId("objektId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objekt_id")
    private ObjektEntity objekt;

    @MapsId("platnostOd")
    @Column(name = "platnost_od")
    private Date platnostOd;

    @Lob
    @Column(name = "otviraci_doba")
    private OtviraciDoba otviraciDoba;
}