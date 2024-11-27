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
    /**
     * This can be a problem, need to verify what data are in DB as bytea
     * in original implementation was used java.util.Collection
     */
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
    @OneToMany(mappedBy = "email_history", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<GeneratedAttachmentEntity> attachments;
    @Column
    private Boolean sent;
}
