package cz.inspire.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedAttachmentDto implements Serializable {
    private String id;
    private String email;
    private Map<String, Object> attributes;
    private String emailHistoryId;
    private String printTemplateId;
}
