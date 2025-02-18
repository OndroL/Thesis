package cz.inspire.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@Entity
@Table(name="mena")
public class MenaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String kod;

    @Column
    private String vycetka;

    @Column
    private int kodNum;

    @Column
    private int zaokrouhleniHotovost;

    @Column
    private int zaokrouhleniKarta;
}