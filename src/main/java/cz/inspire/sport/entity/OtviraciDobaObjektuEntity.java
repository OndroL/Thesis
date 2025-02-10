package cz.inspire.sport.entity;

import cz.inspire.sport.utils.OtviraciDoba;
import jakarta.persistence.*;
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
    private OtviraciDobaObjektuPK id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private OtviraciDoba otviraciDoba;
}