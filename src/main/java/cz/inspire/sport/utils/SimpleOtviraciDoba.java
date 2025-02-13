package cz.inspire.sport.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.utils.PeriodOfTime;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class SimpleOtviraciDoba implements OtviraciDoba {

    private final SortedMap<PeriodOfTime, SportEntity> oteviraciDobaMap;

    @JsonCreator
    public SimpleOtviraciDoba(@JsonProperty("oteviraciDobaMap") SortedMap<PeriodOfTime, SportEntity> oteviraciDobaMap) {
        this.oteviraciDobaMap = oteviraciDobaMap != null ? oteviraciDobaMap : new TreeMap<>();
    }

    public void addTimePeriod(PeriodOfTime period, SportEntity sport) {
        this.oteviraciDobaMap.put(period, sport);
    }

    @Override
    public SortedMap<PeriodOfTime, SportEntity> getOtevreno(Date day) {
        return this.oteviraciDobaMap;
    }

    @Override
    public SortedMap<PeriodOfTime, SportEntity> getOtevreno(Date day, boolean svatkyJakoNormalniDen) {
        return this.oteviraciDobaMap;
    }
}

