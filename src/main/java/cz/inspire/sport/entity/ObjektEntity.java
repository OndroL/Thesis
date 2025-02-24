package cz.inspire.sport.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "objekt")
public class ObjektEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Integer> reservationStart;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Integer> reservationFinish;

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
    @JoinColumn(name = "areal")
    private ArealEntity areal;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "objekt")
    private List<ObjektLocEntity> localeData;

    @OneToMany(mappedBy = "objekt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjektSportEntity> objektSports;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "objektId")
    private List<OvladacObjektuEntity> ovladaceObjektu;

    @OneToMany(mappedBy = "objekt")
    private List<PodminkaRezervaceEntity> podminkyRezervaci;

    @ManyToMany
    @JoinTable(
            name = "nadobjekt_objekt",
            joinColumns = @JoinColumn(name = "objekt"),
            inverseJoinColumns = @JoinColumn(name = "nadobjekt")
    )
    private Set<ObjektEntity> nadObjekty;

    @ManyToMany(mappedBy = "nadObjekty")
    private Set<ObjektEntity> podObjekty;
}
