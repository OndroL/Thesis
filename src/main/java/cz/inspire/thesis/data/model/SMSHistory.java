package cz.inspire.thesis.data.model;

import jakarta.enterprise.context.Dependent;
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
@Dependent
@NoArgsConstructor
@Entity
@Table(name="sms_history")
public class SMSHistory {
    @Id
    private String id;
    @Column
    private Date date;
    @Column
    private String message;
    /**
     * This can be a problem, need to verify what data are in DB as bytea
     * in original implementation was used java.util.Collection
     */
    @Column
    private byte[] groups;
    @Column
    private byte[] recipients;
    @Column
    private byte[] morerecipients;
    @Column
    private Boolean automatic;
}