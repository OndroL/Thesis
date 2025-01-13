package cz.inspire.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeneratedAttachmentDto {
    private String id;
    private String email;
    private Map<String, Object> attributes;
    private String emailHistoryId;
    private String printTemplateId;
}
