package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;

/**
 * TODO : Add Equals ! ! !
 */


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
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private byte[] groups;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private byte[] recipients;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private byte[] moreRecipients;
    @Column
    private Boolean automatic;
    @Column
    private Boolean html;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @OneToMany(mappedBy = "email_history", cascade = CascadeType.ALL)
    private Collection<GeneratedAttachmentEntity> attachments;
    @Column
    private Boolean sent;

    /**
     * Here should also be relation to GeneratedAttachment
     *      * @ejb.interface-method
     *      *    view-type="local"
     *      * @ejb.relation
     *      *    name="generated_attachment-email_history"
     *      *    role-name="email-ma-generovane-prilohy"
     *      * @ejb.value-object match="*"
     *      *    aggregate="java.util.List<GeneratedAttachmentDetails>"
     *      *    aggregate-name="GeneratedAttachments"
     *      *
     * public abstract java.util.Collection getGeneratedAttachments();
     * public abstract void setGeneratedAttachments(java.util.Collection generatedAttachments);
     */
}
