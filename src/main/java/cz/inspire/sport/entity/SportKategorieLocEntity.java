package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
}
