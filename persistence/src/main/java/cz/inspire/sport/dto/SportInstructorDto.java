package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SportInstructorDto implements Serializable {
    private String id;
    private String activityId;
    private String oldSportId;
    private Boolean deleted;
    private String sportId;
    private String instructorId;
}