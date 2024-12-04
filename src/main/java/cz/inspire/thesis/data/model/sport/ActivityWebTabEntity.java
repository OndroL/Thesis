package cz.inspire.thesis.data.model.sport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="activity_webtab")
public class ActivityWebTabEntity {
    @Id
    private String id;
    @Column
    private String sportId;
    @Column
    private String activityId;
    @Column
    private String ObjectId;
    @Column
    private int tabIndex;
}
