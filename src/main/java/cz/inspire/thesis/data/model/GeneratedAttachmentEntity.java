package cz.inspire.thesis.data.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="generated_attachment")
public class GeneratedAttachmentEntity {
    @Id
    private String id;
    @Column
    private String email;
    @Column
    private byte[] attributes;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "email_history", referencedColumnName = "id", nullable = false)
    private EmailHistoryEntity  email_history;

    @ManyToOne
    @JoinColumn(name = "print_template", referencedColumnName = "id", nullable = true)
    private PrintTemplateEntity printTemplate;
}
