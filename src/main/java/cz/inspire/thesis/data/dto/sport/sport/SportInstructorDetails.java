package cz.inspire.thesis.data.dto.sport.sport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SportInstructorDetails {
    private String id;
    private String activityId;
    private String oldSportId;
    private Boolean deleted;
    private String sportId;
    private String instructorId;
}