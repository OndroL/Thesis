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
public class ActivityWebTabDto implements Serializable {
    private String id;
    private String sportId;
    private String activityId;
    private String objectId;
    private int tabIndex;
}