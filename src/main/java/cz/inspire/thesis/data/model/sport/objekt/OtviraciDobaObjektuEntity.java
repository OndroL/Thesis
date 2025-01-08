package cz.inspire.thesis.data.model.sport.objekt;

import cz.inspire.thesis.data.utils.OtviraciDoba;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "otviraci_doba")
public class OtviraciDobaObjektuEntity {
    @EmbeddedId
    private OtviraciDobaObjektuPK id;

    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private OtviraciDoba otviraciDoba;
}