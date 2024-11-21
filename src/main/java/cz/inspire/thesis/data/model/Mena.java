package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Add DB restrictions for NULL
 * */

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="mena")
public class Mena {
    @Id
    private String id;
    @Column
    private String kod;
    @Column
    private String vycetka;
    @Column(nullable = false)
    private int kodnum;
    @Column(nullable = false)
    private int zaokrouhlenihotovost;
    @Column(nullable = false)
    private int zaokrouhlenikarta;
}