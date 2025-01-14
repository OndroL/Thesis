package cz.inspire.sms.entity;

import jakarta.enterprise.context.Dependent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.List;


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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> groups;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> recipients;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> moreRecipients;

    @Column
    private Boolean automatic;
}
