package cz.inspire.thesis.data.model;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Date;



@AllArgsConstructor
@Setter
@Getter
@Dependent
@NoArgsConstructor
@Entity
@Table(name="sms_history")
public class SMSHistoryEntity {
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
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private byte[] groups;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private byte[] recipients;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column
    private byte[] morerecipients;
    @Column
    private Boolean automatic;
}