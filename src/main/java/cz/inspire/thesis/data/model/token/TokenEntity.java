package cz.inspire.thesis.data.model.token;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="token")
public class TokenEntity {
    @Id
    private String id;
    @Column
    private String popis;

    @ManyToOne
    @JoinColumn(name = "typ", referencedColumnName = "id")
    private TypTokenuEntity typTokenu;
}
