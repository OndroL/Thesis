package cz.inspire.thesis.data.model.sport.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="activity_favourite")
public class ActivityFavouriteEntity {
    @Id
    private String id;
    @Column
    private String zakaznikId;
    @Column
    private String activityId;
    @Column
    private int pocet;
    @Column
    private Date datumPosledniZmeny;
}
