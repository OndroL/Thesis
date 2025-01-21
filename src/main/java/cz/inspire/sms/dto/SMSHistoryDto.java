package cz.inspire.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
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