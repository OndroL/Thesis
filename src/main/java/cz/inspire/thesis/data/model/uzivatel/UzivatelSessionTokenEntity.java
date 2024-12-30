package cz.inspire.thesis.data.model.uzivatel;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "uzivatel_session_token")
public class UzivatelSessionTokenEntity {
    @Id
    private String series;
    @Column
    private String username;
    @Column
    private String token;
    @Column
    private Date lastUsed;
}
