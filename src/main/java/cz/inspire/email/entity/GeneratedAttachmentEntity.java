package cz.inspire.email.entity;

import cz.inspire.template.entity.PrintTemplateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="generated_attachment")
public class GeneratedAttachmentEntity {
    @Id
    private String id;

    @Column
    private String email;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> attributes;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_history", referencedColumnName = "id", nullable = false)
    private EmailHistoryEntity emailHistory;

    @ManyToOne
    @JoinColumn(name = "print_template", referencedColumnName = "id")
    private PrintTemplateEntity printTemplate;
}
