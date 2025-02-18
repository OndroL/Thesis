package cz.inspire.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
