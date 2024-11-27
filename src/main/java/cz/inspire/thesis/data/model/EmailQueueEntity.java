package cz.inspire.thesis.data.model;

import jakarta.persistence.*;
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
    private String emailhistory;
    @Column
    private String recipient;
    @Column(nullable = false, columnDefinition = "integer DEFAULT 0")
    private int priority;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean removeemailhistory;
    @Column
    private String dependentemailhistory;
}
