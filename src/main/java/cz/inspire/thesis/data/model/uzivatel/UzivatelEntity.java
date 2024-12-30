package cz.inspire.thesis.data.model.uzivatel;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "uzivatel")
public class UzivatelEntity {
    @Id
    private String login;
    @Column
    private String heslo;

    @Column
    private String jmeno;

    @Column
    private boolean aktivni;

    @Column
    private String aktivacniToken;

    @Column(name = "auth_key")
    private String authKey;

    @Lob
    @Column
    private Map<String, String> nastaveni;

    @ManyToOne
    @JoinColumn(name = "skupina", referencedColumnName = "id")
    private SkupinaEntity skupina;

    @ManyToMany
    @JoinTable(
            name = "uzivatel_role",
            joinColumns = @JoinColumn(name = "login", referencedColumnName = "login"),
            inverseJoinColumns = @JoinColumn(name = "id", referencedColumnName = "id")
    )
    private Collection<RoleEntity> role;

    @OneToMany(mappedBy = "uzivatel", fetch = FetchType.EAGER)
    private Collection<PushKeyEntity> pushKeys;

    @OneToMany(mappedBy = "uzivatel")
    private Collection<UzivatelSessionEntity> sessions;
}

