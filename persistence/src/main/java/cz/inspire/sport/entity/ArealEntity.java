package cz.inspire.sport.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "areal")
public class ArealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private List<ArealEntity> podrazeneArealy = new ArrayList<>();

    @OneToMany(mappedBy = "areal")
    private List<ObjektEntity> objekty = new ArrayList<>();
}

