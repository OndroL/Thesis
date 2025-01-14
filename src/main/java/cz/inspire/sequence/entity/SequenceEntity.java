package cz.inspire.sequence.entity;

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
    private int minValue;
    @Column
    private String last;
    @Column
    private int type;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "stornoseq", referencedColumnName = "name")
    private SequenceEntity stornoSeq;
}
