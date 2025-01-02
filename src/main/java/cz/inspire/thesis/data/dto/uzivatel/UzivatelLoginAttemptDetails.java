package cz.inspire.thesis.data.dto.uzivatel;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UzivatelLoginAttemptDetails {
    private String id;
    private String login;
    private Date vytvoreno;
    private Date blokovanoDo;
    private String ip;
}