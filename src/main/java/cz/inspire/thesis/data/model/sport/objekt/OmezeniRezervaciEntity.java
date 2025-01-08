package cz.inspire.thesis.data.model.sport.objekt;

import cz.inspire.thesis.data.utils.OtviraciDoba;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "omezeni_rezervaci")
public class OmezeniRezervaciEntity implements Serializable {
    @Id
    private String objektId;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private OtviraciDoba omezeni;
}

