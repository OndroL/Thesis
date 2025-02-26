/*
 * TLDataKey.java
 *
 * Created on 4. leden 2004, 17:39
 */

package cz.inspire.utils;

/**
 *
 * @author  dominik
 */
public class TimeOfWeek extends MomentOfTime {
    
    /** Holds value of property dayOfWeek. */
    private int dayOfWeek;
    
    /** Holds value of property timeOfDay. */
    private TimeOfDay timeOfDay;
    
    public TimeOfWeek(int dayOfWeek, TimeOfDay timeOfDay) {
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }
    
    public int compareTo(Object o) {
        TimeOfWeek cKey = (TimeOfWeek) o;
        if (dayOfWeek < cKey.dayOfWeek) return -1;
        if (dayOfWeek > cKey.dayOfWeek) return 1;
        return timeOfDay.compareTo(cKey.timeOfDay);
    }
    
    public boolean equals(Object o) {
        if ( !(o instanceof TimeOfWeek)) return false;
        TimeOfWeek cKey = (TimeOfWeek) o;
        if (o == null) return false;
        return (dayOfWeek == cKey.dayOfWeek) && timeOfDay.equals(cKey.timeOfDay);
    }
    
    public int getMinuteDifference(MomentOfTime difMoment) {
        TimeOfWeek dtow = (TimeOfWeek) difMoment;
        return (dayOfWeek - dtow.dayOfWeek) * 1440 + timeOfDay.getMinuteDifference(dtow.timeOfDay);
    }
    
    /** Getter for property dayOfWeek.
     * @return Value of property dayOfWeek.
     *
     */
    public int getDayOfWeek() {
        return this.dayOfWeek;
    }
    
    /** Setter for property dayOfWeek.
     * @param dayOfWeek New value of property dayOfWeek.
     *
     */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    /** Getter for property timeOfDay.
     * @return Value of property timeOfDay.
     *
     */
    public TimeOfDay getTimeOfDay() {
        return this.timeOfDay;
    }
    
    /** Setter for property timeOfDay.
     * @param timeOfDay New value of property timeOfDay.
     *
     */
    public void setTimeOfDay(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
    
    /* (non-Javadoc)
     * @see cz.inspire.common.util.MomentOfTime#addMinutes(int)
     */
    public MomentOfTime addMinutes(int minutes) {
        return new TimeOfWeek(dayOfWeek, ((TimeOfDay) timeOfDay.addMinutes(minutes)));
    }
    
    public String toString() {
        return dayOfWeek + " -- " + timeOfDay;
    }
    
    public int hashCode() {
        int retValue = dayOfWeek*10000 + timeOfDay.hashCode();
        return retValue;
    }
    
}
