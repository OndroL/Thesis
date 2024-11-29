package cz.inspire.thesis.data.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;


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
    @Column
    private byte[] attributes;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_history", referencedColumnName = "id", nullable = false)
    private EmailHistoryEntity emailHistory;

    @ManyToOne
    @JoinColumn(name = "print_template", referencedColumnName = "id")
    private PrintTemplateEntity printTemplate;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        GeneratedAttachmentEntity obj = (GeneratedAttachmentEntity) o;
        return getId() != null && Objects.equals(getId(), obj.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
