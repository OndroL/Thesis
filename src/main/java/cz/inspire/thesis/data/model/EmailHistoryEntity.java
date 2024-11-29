package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private byte[] attachments;
    @Column
    private Boolean sent;

    @OneToMany(mappedBy = "emailHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GeneratedAttachmentEntity> generatedAttachments;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EmailHistoryEntity obj = (EmailHistoryEntity) o;
        return getId() != null && Objects.equals(getId(), obj.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
