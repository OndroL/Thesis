package cz.inspire.sport.entity;

import cz.inspire.sport.utils.OtviraciDoba;
import cz.inspire.sport.utils.OtviraciDobaConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "omezeni_rezervaci")
public class OmezeniRezervaciEntity implements Serializable {
    @Id
    private String objektId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Convert(converter = OtviraciDobaConverter.class)
    private OtviraciDoba omezeni;
}

