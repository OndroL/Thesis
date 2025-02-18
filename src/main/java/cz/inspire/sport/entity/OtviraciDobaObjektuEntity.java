package cz.inspire.sport.entity;

import cz.inspire.sport.utils.OtviraciDoba;
import cz.inspire.sport.utils.OtviraciDobaConverter;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "otviraci_doba")
public class OtviraciDobaObjektuEntity {
    @EmbeddedId
    private OtviraciDobaObjektuPK embeddedId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Convert(converter = OtviraciDobaConverter.class)
    private OtviraciDoba otviraciDoba;
}