package cz.inspire.sport.entity;

import cz.inspire.sport.dto.SportDto;
import cz.inspire.utils.FileAttributes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name="instructor")
public class InstructorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private FileAttributes photo;

    @Column
    private Boolean deleted;

    @Column
    private String googleCalendarId;

    @Column
    private Boolean googleCalendarNotification;

    @Column
    private int googleCalendarNotificationBefore;

    @OneToMany(mappedBy = "instructor")
    private Set<SportInstructorEntity> sportInstructors;

    @ManyToMany
    @JoinTable(
            name = "instructor_activity",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private Set<ActivityEntity> activities;

    @Transient
    private Set<SportDto> sportSet;
}
