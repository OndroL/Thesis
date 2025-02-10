package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SportInstructorDto implements Serializable {
    private String id;
    private String activityId;
    private String oldSportId;
    private Boolean deleted;
    private String sportId;
    private String instructorId;
}