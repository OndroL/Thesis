package cz.inspire.template.entity;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Dependent
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