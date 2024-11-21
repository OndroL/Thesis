package cz.inspire.thesis.data.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column
    private String stornoseq;

}
