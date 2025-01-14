package cz.inspire.common.entity;

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
    private int zaokrouhleniHotovost;
    @Column
    private int zaokrouhleniKarta;
}