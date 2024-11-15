package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Add DB restrictions for NULL */
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="mena")
public class MenaBean {
    @Id
    private String id;
    @Column
    private String kod;
    @Column
    private String vycetka;
    @Column
    private int kodnum;
    @Column
    private int zaokrouhlenihotovost;
    @Column
    private int zaokrouhlenikarta;
}