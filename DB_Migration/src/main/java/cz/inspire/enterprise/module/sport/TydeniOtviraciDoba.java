/**
 * Clubspire, (c) Inspire CZ 2004-2009
 *
 * TydeniOtviraciDoba.java
 * Created on: 27.7.2004
 * Author: Dominik Pospisil
 *
 */
package cz.inspire.enterprise.module.sport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.inspire.common.util.PeriodOfTime;

import java.io.Serial;
import java.util.Calendar;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Reprezentuje otviraci dobu pro kazdy den v tydnu.
 *
 * @version 1.2
 * @author Dominik Pospisil
 */
@JsonSerialize
@JsonDeserialize
public class TydeniOtviraciDoba<T> implements OtviraciDoba {
    @Serial
    private static final long serialVersionUID = -3916194673397776764L;

    public TydeniOtviraciDoba() { initializeDoby(); }

    public TydeniOtviraciDoba(Class<T> type) { initializeDoby(); }

    @JsonCreator
    public TydeniOtviraciDoba(@JsonProperty("doby") SortedMap<PeriodOfTime, T>[] doby) {
        this.doby = doby;
    }

    public TydeniOtviraciDoba(TydeniOtviraciDoba tydeniOtviraciDoba) {
        for (int i = 0; i < 7; i++) {
            this.doby[i].putAll(tydeniOtviraciDoba.getOtevreno(i));
        }
    }

    /* Index pole znaci den PO=0 ... NE=6.
     * Klic jednotlivych map je interval oteviraci doby.
     * Hodnota mapy je provozovany sport v dany interval.
     */
    @JsonProperty("doby")
    protected SortedMap<PeriodOfTime, T>[] doby;

    private void initializeDoby() {
        this.doby = new SortedMap[]{
                new TreeMap<>(), new TreeMap<>(), new TreeMap<>(),
                new TreeMap<>(), new TreeMap<>(), new TreeMap<>(), new TreeMap<>()
        };
    }

    @JsonProperty("doby")
    public SortedMap<PeriodOfTime, T>[] getDoby() {
        return doby;
    }

    public void setDoby(SortedMap<PeriodOfTime, T>[] doby) {
        this.doby = doby;
    }

//    public CommonController getCommonController() {
//        try { // musim vzdy ziskavat, jinak se ulozi do db
//            CommonController commonController =  CommonControllerUtil.getHome().create();
//            return commonController;
//        } catch (Exception ce) {
//            Logger.getLogger(TydeniOtviraciDoba.class.getName()).error("Unable to get Client controller.", ce);
//        }
//        return null;
//    }
    
//    private boolean isSvatekJakoNormalniDen() {
//        Serializable data = null;
//        try {
//            data = getCommonController().getNastaveni(OtviraciDobaConstants.SVATKY_JAKO_NORMALNI_DEN);
//        } catch (Exception ex) {
//            Logger.getLogger(TydeniOtviraciDoba.class.getName()).info("Nenalezeno nastaveni pro SvatkyJakoNormalniDen", ex);
//        }
//
//        if (data != null) {
//            return (Boolean) data;
//        } else {
//            return false;
//        }
//    }
    
    /**
     * Dotaz na otviraci dobu jednoho dne.
     *
     * @param day den, na jehoz otviraci dobu se ptam
     * @return vrati Mapu. Klic je interval oteviraci doby. Hodnota je provozovany sport v dany interval.
     */
    public SortedMap getOtevreno(Date day) {
       // boolean svatkyJakoNormalniDen = isSvatekJakoNormalniDen();
        return getOtevreno(day, false);
    }
    
//    private ZemeEnum getZeme() {
//        return NastaveniJsonUtils.getSetting(NastaveniJsonKeys.NASTAVENI_LOKALIZACE_ZEME, ZemeEnum.class, ZemeEnum.CZ);
//    }
    
    public SortedMap getOtevreno(Date day, boolean svatkyJakoNormalniDen) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
//        DayOfYear doy = new DayOfYear(ClientUtils.startOfTheDay(day));
//
//        if (!svatkyJakoNormalniDen) {
//            if (Svatky.getInstance(getZeme()).isSvatek(doy)) {
//                return doby[6];
//            }
//        }
        
        int dat = cal.get(Calendar.DAY_OF_WEEK);
        switch (dat) {
            case Calendar.MONDAY: return doby[0];
            case Calendar.TUESDAY: return doby[1];
            case Calendar.WEDNESDAY: return doby[2];
            case Calendar.THURSDAY: return doby[3];
            case Calendar.FRIDAY: return doby[4];
            case Calendar.SATURDAY: return doby[5];
            case Calendar.SUNDAY: return doby[6];
        }
        return null;
    }

    public SortedMap getOtevreno(int dayOfWeek) {
        return doby[dayOfWeek];
    }

    public void addOtevreno(PeriodOfTime time, int day, String sport) {
        doby[day].put(time, (T) sport);
    }

    public void removeOtevreno(PeriodOfTime time, int day) {
        doby[day].remove(time);
    }

    public void updateOtevreno(PeriodOfTime oldTime, PeriodOfTime newTime, int day, String sport) {
        doby[day].remove(oldTime);
        doby[day].put(newTime, (T) sport);
    }
    
    public void setOtevreno(SortedMap casy,int dayOfWeek){
        doby[dayOfWeek] = casy; 
    }

}