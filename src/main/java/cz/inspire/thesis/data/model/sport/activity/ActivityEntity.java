package cz.inspire.thesis.data.model.sport.activity;


import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
    private Set<InstructorEntity> instructors;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<SportEntity> sports;
}