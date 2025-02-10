package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ObjektSportDto {
    private String id;
    private int index;
    private String sportId;
    private String objektId;
}