/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* PeriodOfTimeInDay.java
* Created on: 27.4.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.sport;

import cz.inspire.common.util.PeriodOfTime;

import java.util.Date;
import java.util.Objects;

/**
* PeriodOfTimeInDay
*
* @author <a href="lukas.ondrak@inspire.cz">Lukas Ondrak</a>
*/
public class PeriodOfTimeInDay implements Comparable, java.io.Serializable {
    
    private Date date;
    private PeriodOfTime periodOfTime;

    public PeriodOfTimeInDay(Date date, PeriodOfTime periodOfTime) {
        this.date = date;
        this.periodOfTime = periodOfTime;
    }
    
    @Override
    public int compareTo(Object o) {
        PeriodOfTimeInDay another = (PeriodOfTimeInDay) o;
        return date.compareTo(another.date);
    }

    public PeriodOfTime getPeriodOfTime() {
        return periodOfTime;
    }

    public void setPeriodOfTime(PeriodOfTime periodOfTime) {
        this.periodOfTime = periodOfTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodOfTimeInDay that = (PeriodOfTimeInDay) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(periodOfTime, that.periodOfTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, periodOfTime);
    }
}