package cz.inspire.template.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintTemplateDto implements Serializable {
    private String id;
    private String content;
    private int type;
    private String templateName;
    private String fileName;
}