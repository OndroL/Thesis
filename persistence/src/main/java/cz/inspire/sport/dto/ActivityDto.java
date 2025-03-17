package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private int index;
    private String iconId;
    private List<InstructorDto> instructors = new ArrayList<>();
    private List<SportDto> sports = new ArrayList<>();
}
