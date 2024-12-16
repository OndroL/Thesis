package cz.inspire.thesis.data.dto.sport.activity;

import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityDetails {
    private String id;
    private String name;
    private String description;
    private int index;
    private String iconId;
    private Collection<InstructorDetails> instructors;
    private Collection<SportDetails> sports;
}
