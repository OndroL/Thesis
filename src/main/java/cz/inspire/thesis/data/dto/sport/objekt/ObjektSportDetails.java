package cz.inspire.thesis.data.dto.sport.objekt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ObjektSportDetails {
    private String id;
    private int index;
    private String sportId;
    private String objektId;
}