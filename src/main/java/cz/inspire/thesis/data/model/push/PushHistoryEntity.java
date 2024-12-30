package cz.inspire.thesis.data.model.push;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name="push_history")
public class PushHistoryEntity {
    @Id
    private String id;
    @Column
    private String uzivatelId;
    @Column
    private Boolean read;
    @Column
    private Boolean removed;
    @Column
    private String multicastId;
}
