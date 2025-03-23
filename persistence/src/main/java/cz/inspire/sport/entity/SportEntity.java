package cz.inspire.sport.entity;

import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.utils.SazbaStorna;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sport")
@EqualsAndHashCode(exclude = {"podrazeneSporty", "objekty"})
public class SportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private int typ;

    @Column
    private String zboziId;

    @Column
    private String skladId;

    @Column
    private Integer sazbaJednotek;

    @Column
    private Boolean sazbaNaOsobu;

    @Column
    private Integer sazbaNaCas;

    @Column
    private Boolean uctovatZalohu;

    @Column
    private int podSportyCount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<SazbaStorna> sazbyStorna;

    @Column
    private Integer minDelkaRezervace;

    @Column
    private Integer maxDelkaRezervace;

    @Column
    private Boolean objednavkaZaplniObjekt;

    @Column
    private Integer delkaRezervaceNasobkem;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String barvaPopredi;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String barvaPozadi;

    @Column
    private Boolean zobrazitText;

    @Column
    private Boolean viditelnyWeb;

    @Column
    private Integer navRezervaceOffset;

    @Column
    private Integer delkaHlavniRez;

    @Column
    private Integer minimalniPocetOsob;

    @Column
    private Integer minutyPredVyhodnocenimKapacity;

    @Column
    private Integer maximalniPocetOsobNaZakaznika;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sport")
    private List<SportLocEntity> localeData = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_kategorie", referencedColumnName = "id")
    private SportKategorieEntity sportKategorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nadrazeny_sport", referencedColumnName = "id")
    private SportEntity nadrazenySport;

    @OneToMany(mappedBy = "nadrazenySport")
    private List<SportEntity> podrazeneSporty = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navazujici_sport", referencedColumnName = "id")
    private SportEntity navazujiciSport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private ActivityEntity activity;

    @OneToMany(mappedBy = "sport", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SportInstructorEntity> sportInstructors = new ArrayList<>();

    @OneToMany(mappedBy = "sport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjektSportEntity> objekty = new ArrayList<>();

    @Transient
    private Set<InstructorDto> instructorSet;
}
