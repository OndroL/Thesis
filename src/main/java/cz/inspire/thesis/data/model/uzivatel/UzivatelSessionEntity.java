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
@Table(name = "uzivatel_session")
public class UzivatelSessionEntity {
    @Id
    private String id;
    @Column
    private Date loginTime;
    @Column
    private Date logoutTime;
    @Column
    private int loginType;
    @Column
    private int logoutMethod;
    @Column
    private String ipAddress;
    @Column
    private String clientId;
    @Column
    private Date lastActiveTime;

    @ManyToOne
    @JoinColumn(name = "uzivatel", referencedColumnName = "login")
    private UzivatelEntity uzivatel;

}
