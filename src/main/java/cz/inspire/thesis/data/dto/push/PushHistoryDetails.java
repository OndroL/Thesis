package cz.inspire.thesis.data.dto.push;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushHistoryDetails {
    private String id;
    private String uzivatelId;
    private Boolean read;
    private Boolean removed;
    private String multicastId;
}
