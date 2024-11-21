package cz.inspire.thesis.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SequenceDetails {
    private String name;
    private String pattern;
    private int minValue;
    private String last;
    private int type;
    private String stornoSeqName;
}
