package cz.inspire.thesis.data.model.push;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table
public class PushMulticastEntity {
    @Id
    private String Id;
    @Column
    private Date date;
    @Column
    private String body;
    @Column
    private String historyBody;
    @Column
    private String title;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private byte[] groups;
    @Column
    private Boolean automatic;
    @Column
    private Boolean sent;
    @OneToMany(mappedBy = "pushMulticast", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<PushHistoryEntity> pushHistory;
}
