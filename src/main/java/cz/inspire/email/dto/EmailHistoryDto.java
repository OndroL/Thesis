package cz.inspire.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailHistoryDto implements Serializable {
    private String id;
    private Date date;
    private String text;
    private String subject;
    private List<String> groups;
    private List<String> recipients;
    private List<String> moreRecipients;
    // These are files
    private Map<String, byte[]> attachments;
    private Boolean automatic;
    private Boolean html;
    private Boolean sent;
    private List<GeneratedAttachmentDto> generatedAttachments;
}
