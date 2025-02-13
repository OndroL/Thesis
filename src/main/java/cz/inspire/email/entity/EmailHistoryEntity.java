package cz.inspire.email.entity;

import cz.inspire.utils.FileAttributes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="email_history")
public class EmailHistoryEntity {
    @Id
    private String id;

    @Column
    private Date date;

    @Column
    private String text;

    @Column
    private String subject;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> groups;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> recipients;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> moreRecipients;

    @Column
    private Boolean automatic;

    @Column
    private Boolean html;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<FileAttributes> attachments;

    @Column
    private Boolean sent;

    @OneToMany(mappedBy = "emailHistory", fetch = FetchType.LAZY)
    private List<GeneratedAttachmentEntity> generatedAttachments;
}
