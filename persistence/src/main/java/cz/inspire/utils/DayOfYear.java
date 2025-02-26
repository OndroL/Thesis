/*
 * DayOfYear.java
 *
 * Created on 4. leden 2004, 17:39
 */

package cz.inspire.utils;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author  dominik
 */
public class DayOfYear extends MomentOfTime {
    
    private Calendar cal = Calendar.getInstance();
    
    /** Holds value of property hour. */
    private int month;
    
    /** Holds value of property minute. */
    private int day;

    /** Holds value of property minute. */
    private int year;
    
    /** Creates a new instance of TLDataKey */
    public DayOfYear() {
    }
    
   public DayOfYear(int month, int day) {
        this(0,month,day,0,0);
    }
   
    public DayOfYear(int year, int month, int day) {
        this(year, month, day, 0, 0);
    }
    public DayOfYear(int year, int month, int day, int hour, int minute) {
        this.minute = minute;
        this.hour = hour;
        this.month = month;
        this.day = day;
        this.year = year;
    }
    
        public DayOfYear(Date date) {
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DATE);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);        
    }
    
    public int compareTo(Object o) {
        DayOfYear cKey = (DayOfYear) o;
        if (year < cKey.year) {
            return -1;
        }
        if (year > cKey.year) {
            return 1;
        }
        if (month < cKey.month) {
            return -1;
        }
        if (month > cKey.month){
            return 1;
        }
        if (day < cKey.day) {
            return -1;
        }
        if (day > cKey.day) {
            return 1;
        }
        if (hour < cKey.hour) {
            return -1;
        }
        if (hour > cKey.hour){
            return 1;
        }
        if (minute < cKey.minute) {
            return -1;
        }
        if (minute > cKey.minute){
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if ( !(o instanceof DayOfYear)) {
            return false;
        }
        DayOfYear cKey = (DayOfYear) o;
        if (o == null) {
            return false;
        }
        return (year == cKey.year) && (month == cKey.month) && (day == cKey.day) && 
            (hour == cKey.hour) && (minute == cKey.minute);
    }
    
    /** Getter for property month.
     * @return Value of property month.
     *
     */
    public int getMonth() {
        return this.month;
    }
    
    /** Setter for property month.
     * @param month New value of property month.
     *
     */
    public void setMonth(int month) {
        this.month = month;
    }
    
    /** Getter for property day.
     * @return Value of property day.
     *
     */
    public int getDay() {
        return this.day;
    }
    
    /** Setter for property day.
     * @param day New value of property day.
     *
     */
    public void setDay(int day) {
        this.day = day;
    }

    /** Getter for property day.
     * @return Value of property day.
     *
     */
    public int getYear() {
        return this.year;
    }
    
    /** Setter for property day.
     * @param year New value of property day.
     *
     */
    public void setYear(int year) {
        this.year = year;
    }

    
    public int getMinuteDifference(MomentOfTime difMoment) {
        long time = getDate().getTime();
        DayOfYear ddoy = (DayOfYear) difMoment;
        long difTime = ddoy.getDate().getTime();
        return (int) ((time - difTime) / 1000 / 60);
    }
    
    public MomentOfTime addMinutes(int minutes) {
        long time = getDate().getTime();        
        time += ((long) minutes) * 60L * 1000L;
        cal.setTimeInMillis(time);
        int newYear = cal.get(Calendar.YEAR);
        int newMonth = cal.get(Calendar.MONTH) + 1;
        int newDay = cal.get(Calendar.DATE);
        int newHour = cal.get(Calendar.HOUR_OF_DAY);
        int newMinute = cal.get(Calendar.MINUTE);
        return new DayOfYear(newYear, newMonth, newDay, newHour, newMinute);
    }
    
    public MomentOfTime addDays(int days){
        Date date = getDate();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        int newYear = cal.get(Calendar.YEAR);
        int newMonth = cal.get(Calendar.MONTH) + 1;
        int newDay = cal.get(Calendar.DATE);
        int newHour = cal.get(Calendar.HOUR_OF_DAY);
        int newMinute = cal.get(Calendar.MINUTE);
        return new DayOfYear(newYear, newMonth, newDay, newHour, newMinute);
    }
    
    public Date getDate() {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Holds value of property hour.
     */
    private int hour;

    /**
     * Getter for property hour.
     * @return Value of property hour.
     */
    public int getHour() {

        return this.hour;
    }

    /**
     * Setter for property hour.
     * @param hour New value of property hour.
     */
    public void setHour(int hour) {

        this.hour = hour;
    }

    /**
     * Holds value of property minute.
     */
    private int minute;

    /**
     * Getter for property minute.
     * @return Value of property minute.
     */
    public int getMinute() {

        return this.minute;
    }

    /**
     * Setter for property minute.
     * @param minute New value of property minute.
     */
    public void setMinute(int minute) {

        this.minute = minute;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(String.valueOf(year));
        buf.append("-");
        buf.append(String.valueOf(month));
        buf.append("-");
        buf.append(String.valueOf(day));
        buf.append(" ");
        buf.append(String.valueOf(hour));
        buf.append(":");
        buf.append(String.valueOf(minute));
        return buf.toString();
    }
    
    public int hashCode() {        
        return toString().hashCode();
    }
}
