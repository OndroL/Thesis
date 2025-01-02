package cz.inspire.thesis.data.dto.push;
import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PushMulticastDetails {
    private String id;
    private Date date;
    private String body;
    private String historyBody;
    private String title;
    private byte[] groups;
    private Boolean automatic;
    private Boolean sent;
    private List<PushHistoryDetails> pushHistory;
}
