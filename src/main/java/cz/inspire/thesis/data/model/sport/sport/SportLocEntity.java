package cz.inspire.thesis.data.model.sport.sport;

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
@Table(name="sport_loc")
public class SportLocEntity {
    @Id
    private String id;
    @Column
    private String jazyk;
    @Column
    private String nazev;
    @Column
    private String popis;

}
