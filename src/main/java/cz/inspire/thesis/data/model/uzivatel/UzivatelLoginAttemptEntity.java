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
@Table(name = "uzivatel_login_attempt")
public class UzivatelLoginAttemptEntity {
    @Id
    private String id;
    @Column
    private String login;
    @Column
    private Date created;
    @Column
    private Date blockedTillTime;
    @Column
    private String ip;
}
