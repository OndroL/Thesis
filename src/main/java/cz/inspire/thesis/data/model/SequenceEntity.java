package cz.inspire.thesis.data.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="seq")
public class SequenceEntity {
    @Id
    private String name;
    @Column
    private String pattern;
    @Column
    private int minvalue;
    @Column
    private String last;
    @Column
    private int type;
    @OneToOne
    @JoinColumn(name = "storno_seq", referencedColumnName = "name")
    private SequenceEntity stornoSeq;
}
