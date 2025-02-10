package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "ovladac_objektu")
public class OvladacObjektuEntity {
    @Id
    private String id;
    @Column
    private String idOvladace;
    @Column
    private String cislaZapojeni;
    @Column
    private Boolean manual;
    @Column
    private Boolean automat;
    @Column
    private int delkaSepnutiPoKonci;
    @Column
    private int zapnutiPredZacatkem;
    //This means that, attribute cislaZapojeniList is not persisted in DB as it is in Bean
    @Transient
    private List<Integer> cislaZapojeniList;
    @Column
    private String objektId;
}

