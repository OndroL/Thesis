package cz.inspire.thesis.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailQueueDetails {
    private String id;
    private Date created;
    private String emailHistory;
    private String recipient;
    private int priority;
    private boolean removeEmailHistory;
    private String dependentEmailHistory;

}
