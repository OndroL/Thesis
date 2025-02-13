package cz.inspire.sport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
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
@Table(name="activity")
public class ActivityEntity {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int index;
    @Column
    private String iconId;

    @ManyToMany(mappedBy = "activities")
    private List<InstructorEntity> instructors;

    @OneToMany(mappedBy = "activity")
    private List<SportEntity> sports;
}
