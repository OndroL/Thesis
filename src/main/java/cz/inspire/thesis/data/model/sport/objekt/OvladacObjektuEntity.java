package cz.inspire.thesis.data.model.sport.objekt;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
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
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private List<Integer> cislaZapojeniList;
    @Column
    private String objektId;
}

