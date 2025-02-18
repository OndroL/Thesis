package cz.inspire.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ActivityDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private int index;
    private String iconId;
    private List<InstructorDto> instructors;
    private List<SportDto> sports;
}
