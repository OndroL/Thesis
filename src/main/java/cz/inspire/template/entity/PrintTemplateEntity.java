package cz.inspire.template.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="print_template")
public class PrintTemplateEntity {
    @Id
    private String id;

    @Column
    private String content;

    @Column
    private int type;

    @Column
    private String templateName;

    @Column
    private String fileName;
}