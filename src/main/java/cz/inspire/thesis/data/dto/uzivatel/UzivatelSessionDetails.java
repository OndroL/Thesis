package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UzivatelSessionDetails {
    private String id;
    private Date loginTime;
    private Date logoutTime;
    private int loginType;
    private int logoutMethod;
    private String uzivatel;
    private String ipAddress;
    private String clientId;
    private Date lastActiveTime;
}