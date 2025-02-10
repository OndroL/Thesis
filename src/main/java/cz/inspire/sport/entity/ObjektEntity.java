package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;
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
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Integer[] reservationStart;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
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
    @JoinColumn(name = "areal")
    private ArealEntity areal;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "objekt")
    private Map<String, ObjektLocEntity> localeData;

    @OneToMany(mappedBy = "objekt")
    private List<ObjektSportEntity> objektSports;

    @OneToMany(mappedBy = "objektId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OvladacObjektuEntity> ovladaceObjektu;

    @OneToMany(mappedBy = "objekt")
    private List<PodminkaRezervaceEntity> podminkyRezervaci;

    @ManyToMany
    @JoinTable(
            name = "nadobjekt_objekt",
            joinColumns = @JoinColumn(name = "objekt"),
            inverseJoinColumns = @JoinColumn(name = "nadobjekt")
    )
    private Set<ObjektEntity> nadobjekty;

    @ManyToMany(mappedBy = "nadobjekty")
    private Set<ObjektEntity> podobjekty;
}
