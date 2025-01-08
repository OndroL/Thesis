package cz.inspire.thesis.data.model.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="instructor")
public class InstructorEntity {
    @Id
    private String id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private int index;
    @Column
    private String email;
    @Column
    private String phoneCode;
    @Column
    private String phoneNumber;
    @Column
    private String emailInternal;
    @Column
    private String phoneCodeInternal;
    @Column
    private String phoneNumberInternal;
    @Column
    private String info;
    @Column
    private String color;
    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    private byte[] photo;
    @Column
    private Boolean deleted;
    @Column
    private String googleCalendarId;
    @Column
    private Boolean googleCalendarNotification;
    @Column
    private int googleCalendarNotificationBefore;

    @OneToMany(mappedBy = "instructor")
    private Collection<SportInstructorEntity> sportInstructors;

    @ManyToMany
    @JoinTable(
            name = "instructor_activity",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private Collection<ActivityEntity> activities;

    @Transient
    private Set<SportDetails> sportSet;
}
