package cz.inspire.sequence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="seq")
public class SequenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
