package cz.inspire.thesis.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMSHistoryDetails {
    private String id;
    private Date date;
    private String message;
    private List<String> groups;
    private List<String> recipients;
    private List<String> moreRecipients;
    private Boolean automatic;
}
