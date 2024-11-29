package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="email_queue")
public class EmailQueueEntity {
    @Id
    private String id;
    @Column
    private Date created;
    @Column
    private String emailhistory;
    @Column
    private String recipient;
    @Column(nullable = false, columnDefinition = "integer DEFAULT 0")
    private int priority;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean removeemailhistory;
    @Column
    private String dependentemailhistory;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EmailQueueEntity obj = (EmailQueueEntity) o;
        return getId() != null && Objects.equals(getId(), obj.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
