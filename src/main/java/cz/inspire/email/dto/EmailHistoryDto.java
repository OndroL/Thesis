package cz.inspire.email.dto;

import cz.inspire.email.entity.GeneratedAttachmentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailHistoryDto {
    private String id;
    private Date date;
    private String text;
    private String subject;
    private List<String> groups;
    private List<String> recipients;
    private List<String> moreRecipients;
    private List<String> attachments;
    private Boolean automatic;
    private Boolean html;
    private Boolean sent;

    /**
     * This is added to mimic the implementation of postCreate in EmailHistoryService
     * for adding Attachment to emails in DB
     */
    private List<GeneratedAttachmentDto> generatedAttachments;
}
