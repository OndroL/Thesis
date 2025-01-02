package cz.inspire.thesis.data.model.sport.sport;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="sport_instructor")
public class SportInstructorEntity {
    @Id
    private String id;
    @Column
    private String activityId;
    @Column
    private String oldSportId;
    @Column
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "sport_id")
    private SportEntity sport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "instructor_id")
    private InstructorEntity instructor;
}
