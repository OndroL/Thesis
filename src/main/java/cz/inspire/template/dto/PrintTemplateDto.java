package cz.inspire.template.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintTemplateDto {
    private String id;
    private String content;
    private int type;
    private String templateName;
    private String fileName;
}