package cz.inspire.thesis.data.dto.sport.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
    private Set<String> instructors;
}
