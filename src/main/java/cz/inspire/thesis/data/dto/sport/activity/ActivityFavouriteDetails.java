package cz.inspire.thesis.data.dto.sport.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityFavouriteDetails {
    private String id;
    private String zakaznikId;
    private String activityId;
    private int pocet;
    private Date datumPosledniZmeny;
}