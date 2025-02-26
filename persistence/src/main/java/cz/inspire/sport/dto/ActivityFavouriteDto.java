package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityFavouriteDto implements Serializable {
    private String id;
    private String zakaznikId;
    private String activityId;
    private int pocet;
    private Date datumPosledniZmeny;
}