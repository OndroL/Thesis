/*
 * Util-Time, (c) Inspire CZ 2004-2006
 *
 * PeriodOfTime.java, verze 1.1
 * Vytvoreno: 4. leden 2004
 * Autor: dominik
 *
 */


package cz.inspire.utils;

import java.util.Objects;

/**
 * Objekt reprezentuje casovy interval.
 *
 * @author  dominik
 */
public class PeriodOfTime implements Comparable, java.io.Serializable {
    //9216436376328089481, local class serialVersionUID = -2599936183470225986
    private static final long serialVersionUID = 9216436376328089481L;
    
    public static final PeriodOfTime NULL_PERIOD =
        new PeriodOfTime(new TimeOfDay(0, 0), new TimeOfDay(0, 0));
    
    /** Holds value of property begin. */
    private MomentOfTime begin;
    
    /** Holds value of property end. */
    private MomentOfTime end;
    
    /** Creates a new instance of PeriodOfTime */
    public PeriodOfTime() {
    }
    /** Creates a new instance of PeriodOfTime */
    public PeriodOfTime(MomentOfTime begin, MomentOfTime end) {
        this.begin = begin;
        this.end = end;
    }
    
    public int compareTo(Object o) {
        PeriodOfTime cInt = (PeriodOfTime) o;
        int cBegin = begin.compareTo(cInt.begin);
        if (cBegin != 0) return cBegin;
        
        return end.compareTo(cInt.end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodOfTime that = (PeriodOfTime) o;
        return Objects.equals(begin, that.begin) &&
                Objects.equals(end, that.end);
    }
    
    /** Getter for property begin.
     * @return Value of property begin.
     *
     */
    public MomentOfTime getBegin() {
        return this.begin;
    }
    
    /** Setter for property begin.
     * @param begin New value of property begin.
     *
     */
    public void setBegin(MomentOfTime begin) {
        this.begin = begin;
    }
    
    /** Getter for property end.
     * @return Value of property end.
     *
     */
    public MomentOfTime getEnd() {
        return this.end;
    }
    
    /** Setter for property end.
     * @param end New value of property end.
     *
     */
    public void setEnd(MomentOfTime end) {
        this.end = end;
    }
    
    public int getMinutes() {
        return end.getMinuteDifference(begin);
    }
    
    public boolean contains(MomentOfTime time) {
        return (begin.compareTo(time) <= 0) && (end.compareTo(time) >=0);
    }

    public PeriodOfTime intersect(PeriodOfTime ip) {
        MomentOfTime ibegin = begin;
        if (begin.compareTo(ip.begin) < 0) ibegin = ip.begin;
        MomentOfTime iend = end;
        if (end.compareTo(ip.end) > 0) iend = ip.end;
        if (ibegin.compareTo(iend) >= 0) return NULL_PERIOD;
        return new PeriodOfTime(ibegin, iend);
    }
    
    public MomentOfTime getMiddle() {
        return begin.addMinutes(getMinutes() / 2);
    }
    
    
    @Override
    public String toString() {
        return (begin == null ? "null" : begin.toString())
                + " - " + (end == null ? "null" : end.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end);
    }
}
