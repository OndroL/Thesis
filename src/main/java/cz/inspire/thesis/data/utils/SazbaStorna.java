package cz.inspire.thesis.data.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SazbaStorna {
    private Double poplatek;
    private Integer cas;
    private String sportId;

}
