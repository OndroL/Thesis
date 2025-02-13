/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* PeriodOfTimeInDay.java
* Created on: 27.4.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.utils;

import java.util.Date;

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
}