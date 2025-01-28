package cz.inspire.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailQueueDto implements Serializable {
    private String id;
    private Date created;
    private String emailHistory;
    private String recipient;
    private int priority;
    private boolean removeEmailHistory;
    private String dependentEmailHistory;

}
