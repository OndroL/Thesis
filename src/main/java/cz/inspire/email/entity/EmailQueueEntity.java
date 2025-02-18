package cz.inspire.email.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name="email_queue")
public class EmailQueueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private Date created;

    @Column
    private String emailHistory;

    @Column
    private String recipient;

    @Column
    private int priority;

    @Column
    private boolean removeEmailHistory;

    @Column
    private String dependentEmailHistory;
}