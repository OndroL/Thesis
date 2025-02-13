package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private int index;
    private String iconId;
    private List<InstructorDto> instructors;
    private List<SportDto> sports;
}
