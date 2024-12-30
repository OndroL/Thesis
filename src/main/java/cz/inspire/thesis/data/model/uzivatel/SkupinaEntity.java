package cz.inspire.thesis.data.model.uzivatel;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Map;
import java.util.Collection;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "skupina")
public class SkupinaEntity {
    @Id
    private String id;
    @Column
    private String nazev;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private Map<String, String> nastaveni;

    @ManyToMany
    @JoinTable(
            name = "skupina_role",
            joinColumns = @JoinColumn(name = "skupina", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "id")
    )
    private Collection<RoleEntity> role;

    @OneToMany(mappedBy = "skupina")
    private Collection<UzivatelEntity> uzivatele;
}