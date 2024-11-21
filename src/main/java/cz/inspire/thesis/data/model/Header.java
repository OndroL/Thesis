package cz.inspire.thesis.data.model;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Setter
@Getter
@Dependent
@NoArgsConstructor
@Entity
@Table(name="noteheader")
public class Header {
    @Id
    private String id;
    @Column
    private int field;
    @Column
    private int location;
}
