package cz.inspire.thesis.data.model.sport.sport;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


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
    private List<SportKategorieLocEntity> localeData;

    @OneToMany(mappedBy = "sportKategorie")
    private List<SportEntity> cinnosti;

}
