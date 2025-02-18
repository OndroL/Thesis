package cz.inspire.email.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class EmailQueueDto implements Serializable {
    private String id;
    private Date created;
    private String emailHistory;
    private String recipient;
    private int priority;
    private boolean removeEmailHistory;
    private String dependentEmailHistory;

}
