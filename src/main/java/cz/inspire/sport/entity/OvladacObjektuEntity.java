package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ovladac_objektu")
public class OvladacObjektuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @Column
    private String idOvladace;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Integer> cislaZapojeni;

    @Column
    private Boolean manual;

    @Column
    private Boolean automat;

    @Column
    private int delkaSepnutiPoKonci;

    @Column
    private int zapnutiPredZacatkem;

    @Column
    private String objektId;
}

