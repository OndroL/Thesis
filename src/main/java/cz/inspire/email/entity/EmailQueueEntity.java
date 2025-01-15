package cz.inspire.email.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="email_queue")
public class EmailQueueEntity {
    @Id
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