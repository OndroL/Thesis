package cz.inspire.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeneratedAttachmentDto implements Serializable {
    private String id;
    private String email;
    private Map<String, Object> attributes;
    private String emailHistoryId;
    private String printTemplateId;
}
