package cz.inspire.thesis.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name="license")
public class LicenseEntity {
    @Id
    private String id;
    @Column
    private String customer;
    @Column
    private Boolean valid;
    @Column
    private Boolean centerOnline;
    @Column
    private Date validFrom;
    @Column
    private Boolean validFromSet;
    @Column
    private Date validTo;
    @Column
    private Boolean validToSet;
    @Column
    private Integer activityLimit;
    @Column
    private Integer sportCenterLimit;
    @Column
    private Integer sportCustomersLimit;
    @Column
    private Integer usersLimit;
    @Column
    private Integer customerGroupsLimit;
    @Column
    private Integer pokladnaLimit;
    @Column
    private Integer skladLimit;
    @Column
    private Integer maxClients;
    @Column
    private Boolean ovladaniQuido;
    @Column
    private Long modules;
    @Column
    private String hash;
    @Column
    private Date createdDate;
    @Column
    private Date generatedDate;
    @Column
    private int centerId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LicenseEntity obj = (LicenseEntity) o;
        return getId() != null && Objects.equals(getId(), obj.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
