package cz.inspire.thesis.data.model.sport.sport;

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
@Table(name="sport_kategorie_loc")
public class SportKategorieLocEntity {
    @Id
    private String id;
    @Column
    private String jazyk;
    @Column
    private String nazev;
    @Column
    private String popis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sportKategorie", nullable = false)
    private SportKategorieEntity sportKategorie;
}
