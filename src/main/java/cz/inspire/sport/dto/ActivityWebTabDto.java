package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityWebTabDto {
    private String id;
    private String sportId;
    private String activityId;
    private String objectId;
    private int tabIndex;
}