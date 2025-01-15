package cz.inspire.sequence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
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
