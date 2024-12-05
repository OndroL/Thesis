package cz.inspire.thesis.data.model.sport.objekt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "objekt")
public class ObjektEntity {
    @Id
    private String id;
    @Column
    private int kapacita;
    @Column
    private int casovaJednotka;
    @Column
    private int typRezervace;
    @Column
    private boolean primyVstup;
    @Column
    private Integer minDelkaRezervace;
    @Column
    private Integer maxDelkaRezervace;
    @Column
    private Integer volnoPredRezervaci;
    @Column
    private Integer volnoPoRezervaci;
    @Column
    private Integer zarovnatZacatekRezervace;
    @Column
    private Integer delkaRezervaceNasobkem;
    @Column
    private Boolean vicestavovy;
    @Column
    private Integer stav;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private Integer[] reservationStart;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private Integer[] reservationFinish;
    @Column
    private boolean odcitatProcedury;
    @Column
    private boolean rezervaceNaTokeny;
    @Column
    private boolean rucniUzavreniVstupu;
    @Column
    private boolean upraveniCasuVstupu;
    @Column
    private boolean pozastavitVstup;
    @Column
    private boolean showProgress;
    @Column
    private boolean checkTokensCount;
    @Column
    private boolean selectInstructor;
    @Column
    private boolean showInstructorName;
    @Column
    private boolean showSportName;
    @Column
    private Integer vytvoreniRezervacePredZacatkem;
    @Column
    private Integer editaceRezervacePredZacatkem;
    @Column
    private Integer zruseniRezervacePredZacatkem;
    @Column
    private String googleCalendarId;
    @Column
    private boolean googleCalendarNotification;
    @Column
    private int googleCalendarNotificationBefore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areal", nullable = false)
    private ArealEntity areal;

    @OneToMany(mappedBy = "objekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ObjektLocEntity> localeData;

    @OneToMany(mappedBy = "objekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ObjektSportEntity> objektSports;

    @OneToMany(mappedBy = "objekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<OtviraciDobaObjektuEntity> openingHours;

    @OneToMany(mappedBy = "objekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<OvladacObjektuEntity> ovladaceObjektu;

    @OneToMany(mappedBy = "objekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<PodminkaRezervaceEntity> podminkyRezervaci;

    @ManyToMany
    @JoinTable(
            name = "nadobjekt_objekt",
            joinColumns = @JoinColumn(name = "objekt_id"),
            inverseJoinColumns = @JoinColumn(name = "nadobjekt_id")
    )
    private Set<ObjektEntity> nadobjekty;

    @ManyToMany(mappedBy = "nadobjekty")
    private Set<ObjektEntity> podobjekty;
}
