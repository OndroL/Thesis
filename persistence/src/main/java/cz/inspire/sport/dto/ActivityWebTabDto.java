package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityWebTabDto implements Serializable {
    private String id;
    private String sportId;
    private String activityId;
    private String objectId;
    private int tabIndex;
}