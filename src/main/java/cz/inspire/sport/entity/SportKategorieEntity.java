package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sport_kategorie")
public class SportKategorieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private List<SportKategorieLocEntity> localeData;

    @OneToMany(mappedBy = "sportKategorie")
    private List<SportEntity> cinnosti;

}
