package cz.inspire.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityFavouriteDto implements Serializable {
    private String id;
    private String zakaznikId;
    private String activityId;
    private int pocet;
    private Date datumPosledniZmeny;
}