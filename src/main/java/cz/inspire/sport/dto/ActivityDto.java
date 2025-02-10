package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityDto {
    private String id;
    private String name;
    private String description;
    private int index;
    private String iconId;
    private Collection<InstructorDto> instructors;
    private Collection<SportDto> sports;
}
