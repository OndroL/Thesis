package cz.inspire.thesis.data.model.uzivatel;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "push_key")
public class PushKeyEntity {
    @Id
    private String id;
    @Column(nullable = false)
    private String key;

    @ManyToOne
    @JoinColumn(name = "uzivatel_id", referencedColumnName = "login", nullable = true)
    private UzivatelEntity uzivatel;

}
