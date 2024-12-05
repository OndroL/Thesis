package cz.inspire.thesis.data.model.sport.sport;

import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektSportEntity;
import cz.inspire.thesis.data.utils.SazbaStorna;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.awt.Color;
import java.sql.Types;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="sport")
public class SportEntity {
    @Id
    private String id;
    @Column
    private String type;
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
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private List<SazbaStorna> sazbyStorna;
    @Column
    private Integer minDelkaRezervace;
    @Column
    private Integer maxDelkaRezervace;
    @Column
    private Boolean objednavkaZaplniObjekt;
    @Column
    private Integer delkaRezervaceNasobkem;
    @Column
    private Color barvaPopredi;
    @Column
    private Color barvaPozadi;
    @Column
    private Boolean zobrazitText;
    /**
     * Setter in old bean look's like this -> public abstract void setViditelnyWeb(boolean uctovatZalohu);
     */
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
    private List<SportLocEntity> localeData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_kategorie", referencedColumnName = "id", nullable = false)
    private SportKategorieEntity sportKategorie;

    /**
     * I was checking the DB with data present in it and found that no row has any nadrazenySport
     * present, so maybe this is dead functionality
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nadrazeny_sport", referencedColumnName = "id")
    private SportEntity nadrazenySport;

    @OneToMany(mappedBy = "nadrazenySport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SportEntity> podrazeneSporty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "navazujici_sport", referencedColumnName = "id")
    private SportEntity navazujiciSport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private ActivityEntity activity;

    @OneToMany(mappedBy = "sport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SportInstructorEntity> sportInstructors;

    @OneToMany(mappedBy = "sport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjektSportEntity> objekty;
}
