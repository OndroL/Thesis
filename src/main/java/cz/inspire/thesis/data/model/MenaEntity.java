package cz.inspire.thesis.data.model;

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
@Table(name="mena")
public class MenaEntity {
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