package cz.inspire.thesis.data.model.sport;

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
@Table(name="sport_kategorie")
public class SportKategorieEntity {
    @Id
    private String id;
    @Column
    private String multiSportFacilityId;
    @Column
    private String multiSportServiceUUID;

}
