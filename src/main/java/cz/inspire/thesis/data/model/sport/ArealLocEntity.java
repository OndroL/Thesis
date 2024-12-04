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
@Table(name = "areal_loc")
public class ArealLocEntity {

    @Id
    private String id;

    @Column
    private String jazyk;

    @Column
    private String nazev;

    @Column
    private String popis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areal", nullable = false)
    private ArealEntity areal;
}
