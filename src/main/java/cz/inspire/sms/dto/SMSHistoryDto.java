package cz.inspire.sms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SMSHistoryDto implements Serializable {
    private String id;
    private Date date;
    private String message;
    private List<String> groups;
    private List<String> recipients;
    private List<String> moreRecipients;
    private Boolean automatic;
}