package cz.inspire.sport.entity;

import cz.inspire.sport.utils.OtviraciDoba;
import cz.inspire.sport.utils.OtviraciDobaConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otviraci_doba")
public class OtviraciDobaObjektuEntity {
    @EmbeddedId
    // Eventually add @OneToOne Relationship or move omezeni column into ObjektEntity
    private OtviraciDobaObjektuPK embeddedId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Convert(converter = OtviraciDobaConverter.class)
    private OtviraciDoba otviraciDoba;
}