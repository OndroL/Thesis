package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "areal")
public class ArealEntity {
    @Id
    private String id;
    @Column
    private Integer pocetNavazujucichRez;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "areal")
    private List<ArealLocEntity> localeData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nadrazeny_areal", referencedColumnName = "id")
    private ArealEntity nadrazenyAreal;

    @OneToMany(mappedBy = "nadrazenyAreal")
    private List<ArealEntity> podrazeneArealy;

    @OneToMany(mappedBy = "areal")
    private List<ObjektEntity> objekty;
}

