package cz.inspire.thesis.data.model.uzivatel;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "role")
public class RoleEntity {
    @Id
    private String id;
    @Column
    private String nazev;
    @Column
    private String popis;
}
