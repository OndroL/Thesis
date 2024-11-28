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
    @Column(nullable = false)
    private int minvalue;
    @Column
    private String last;
    @Column
    private int type;
    /**
     * This is only placeholder, maybe a correct one for sklad_seq
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sklad_seq", referencedColumnName = "sklad")
    private SkladSequenceEntity stornoSeq;

}
