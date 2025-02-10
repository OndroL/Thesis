package cz.inspire.sport.entity;

import cz.inspire.sport.dto.SportDto;
import cz.inspire.utils.File;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
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
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    // File from cz.inspire.utils.File;
    // Maybe rename to something else like FsFile
    private List<File> photo;
    @Column
    private Boolean deleted;
    @Column
    private String googleCalendarId;
    @Column
    private Boolean googleCalendarNotification;
    @Column
    private int googleCalendarNotificationBefore;

    @OneToMany(mappedBy = "instructor")
    private List<SportInstructorEntity> sportInstructors;

    @ManyToMany
    @JoinTable(
            name = "instructor_activity",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<ActivityEntity> activities;

    @Transient
    private Set<SportDto> sportSet;
}
