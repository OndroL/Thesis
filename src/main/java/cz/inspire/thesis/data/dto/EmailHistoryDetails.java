package cz.inspire.thesis.data.dto;

import cz.inspire.thesis.data.model.GeneratedAttachmentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailHistoryDetails {
    private String id;
    private Date date;
    private String text;
    private String subject;
    private Collection<String> groups;
    private Collection<String> recipients;
    private Collection<String> moreRecipients;
    private Boolean automatic;
    private Boolean html;
    private Boolean sent;

    /**
     * This is added to mimic the implementation of postCreate in EmailHistoryService
     * for adding Attachment to emails in DB
     */
    private Collection<GeneratedAttachmentDetails> generatedAttachments;
}
