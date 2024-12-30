package cz.inspire.thesis.data.model.token;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "typ_tokenu_loc")
public class TypTokenuLocEntity {
    @Id
    private String id;
    @Column
    private String jazyk;
    @Column
    private String nazev;
    @Column
    private String popis;
}