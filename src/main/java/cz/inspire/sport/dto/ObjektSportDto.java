package cz.inspire.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ObjektSportDto implements Serializable {
    private String id;
    private int index;
    private String sportId;
    private String objektId;
}