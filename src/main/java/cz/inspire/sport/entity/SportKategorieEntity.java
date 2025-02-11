package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="sport_kategorie")
public class SportKategorieEntity {
    @Id
    private String id;
    @Column
    private String multiSportFacilityId;
    @Column
    private String multiSportServiceUUID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nadrazena_kategorie", referencedColumnName = "id")
    private SportKategorieEntity nadrazenaKategorie;

    @OneToMany(mappedBy = "nadrazenaKategorie")
    private List<SportKategorieEntity> podrazeneKategorie;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sportKategorie")
    @MapKey(name="id")
    private Map<String, SportKategorieLocEntity> localeData;

    @OneToMany(mappedBy = "sportKategorie")
    private List<SportEntity> cinnosti;

}
