package cz.inspire.thesis.data.dto.sport.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityWebTabDetails {
    private String id;
    private String sportId;
    private String activityId;
    private String objectId;
    private int tabIndex;
}