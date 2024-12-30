package cz.inspire.thesis.data.model.token;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="typ_tokenu")
public class TypTokenuEntity {
    @Id
    private String id;
    @Column
    private boolean autoPrirazovatZakaznikum;
    @Column
    private boolean zobrazovatNazevTokenu;

    @OneToMany(mappedBy = "typTokenu", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<TokenEntity> tokeny;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "typ_tokenu")
    private Collection<TypTokenuLocEntity> localeData;

}
