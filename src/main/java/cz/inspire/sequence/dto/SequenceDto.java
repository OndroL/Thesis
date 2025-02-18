package cz.inspire.sequence.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SequenceDto implements Serializable {
    private String name;
    private String pattern;
    private int minValue;
    private String last;
    private int type;
    private String stornoSeqName;
}
