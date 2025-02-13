/**
 * Clubspire, (c) Inspire CZ 2004-2009
 *
 * TimeOfDay.java
 * Created on: 4.1.2004
 * Author: Dominik Pospisil
 *
 */
package cz.inspire.utils;

import java.util.Date;
import java.util.Calendar;

/**
 * Reprezentuje urcity cas v ramci jednoho dne. Napr. 15:50.
 *
 * @version 1.0
 * @author Dominik Pospisil
 */
public class TimeOfDay extends MomentOfTime {
    //stream classdesc serialVersionUID = 7052004693300424169, local class serialVersionUID = -4003080771627345189
    private static final long serialVersionUID = 7052004693300424169L;
    
    /** Holds value of property hour. */
    private int hour;
    
    /** Holds value of property minute. */
    private int minute;
    
    /** Creates a new instance of TLDataKey */
    public TimeOfDay() {
    }
    public TimeOfDay(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
    
    public TimeOfDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
    }
    
    public int compareTo(Object o) {
        TimeOfDay cKey = (TimeOfDay) o;
        if (hour < cKey.hour) return -1;
        if (hour > cKey.hour) return 1;
        if (minute < cKey.minute) return -1;
        if (minute > cKey.minute) return 1;
        return 0;
    }
    
    public Date getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, getHour());
        calendar.set(Calendar.MINUTE, getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        return calendar.getTime();
    }
    
    @Override
    public boolean equals(Object o) {
        if ( !(o instanceof TimeOfDay)) return false;
        TimeOfDay cKey = (TimeOfDay) o;
        if (o == null) return false;
        return (hour == cKey.hour) && (minute == cKey.minute);
    }
    
    /** Getter for property hour.
     * @return Value of property hour.
     *
     */
    public int getHour() {
        return this.hour;
    }
    
    /** Setter for property hour.
     * @param hour New value of property hour.
     *
     */
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    /** Getter for property minute.
     * @return Value of property minute.
     *
     */
    public int getMinute() {
        return this.minute;
    }
    
    /** Setter for property minute.
     * @param minute New value of property minute.
     *
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public int getMinuteDifference(MomentOfTime difMoment) {
        TimeOfDay dtod = (TimeOfDay) difMoment;
        return (hour - dtod.hour) * 60 + (minute - dtod.minute);
    }

    @Override
    public MomentOfTime addMinutes(int minutes) {
        int minutesRes = hour*60 + minute + minutes;
        return new TimeOfDay(minutesRes / 60, minutesRes % 60);        
    }
    
    public MomentOfTime subtractMinutes(int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.add(Calendar.MINUTE, -minutes);
        return new TimeOfDay(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));        
    }
    
    @Override
    public String toString() {
        return hour + ":" + (minute > 9 ? "" : "0") + minute;
    }

    @Override
    public int hashCode() {
        int retValue = hour*100 + minute;
        return retValue;
    }
}
