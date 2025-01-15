package cz.inspire.sequence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SequenceDto {
    private String name;
    private String pattern;
    private int minValue;
    private String last;
    private int type;
    private String stornoSeqName;
}
