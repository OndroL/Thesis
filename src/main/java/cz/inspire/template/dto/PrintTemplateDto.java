package cz.inspire.template.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PrintTemplateDto implements Serializable {
    private String id;
    private String content;
    private int type;
    private String templateName;
    private String fileName;
}