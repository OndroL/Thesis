package cz.inspire.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 *
 * @author Dusan
 */
@JsonSerialize
@JsonDeserialize
public class RozsirenaTydenniOtviraciDoba extends TydeniOtviraciDoba {

    public RozsirenaTydenniOtviraciDoba() {
        super(List.class);
    }

    @JsonCreator
    public RozsirenaTydenniOtviraciDoba(@JsonProperty("doby") SortedMap<PeriodOfTime, List<String>>[] doby) {
        super(doby);
    }


    /**
     * Prida interval time, kedy je otvorene k danemu dnu v tyzdni
     *
     * @param time casovy rozsah
     * @param day den v tyzdni
     * @param id id sportu alebo id skupiny (pri omezeni rezervaci na webe)
     */
    @Override
    public void addOtevreno(PeriodOfTime time, int day, String id) {
        List<String> ids = (List<String>) doby[day].get(time);

        if (ids == null) {
            ids = new ArrayList<>();
            doby[day].put(time, ids);
        }

        if (!ids.contains(id)) {
            ids.add(id);
        }
    }

    /**
     * Updatne staru otvaraciu dobu
     * @param oldTime stary casovy interval
     * @param newTime novy casovy interval
     * @param day den v tyzdni
     * @param id id sportu alebo id skupiny (pri omezeni rezervaci na webe)
     */
    @Override
    public void updateOtevreno(PeriodOfTime oldTime, PeriodOfTime newTime, int day, String id) {
        List ids = (List) super.doby[day].get(oldTime);
        super.doby[day].remove(oldTime);
        super.doby[day].put(newTime, ids);
    }
    
    /**
     * Updatne staru otvaraciu dobu
     * @param oldTime stary casovy interval
     * @param newTime novy casovy interval
     * @param day den v tyzdni
     * @param ids id sportov alebo id skupin (pri omezeni rezervaci na webe)
     */
    public void updateOtevreno(PeriodOfTime oldTime, PeriodOfTime newTime, int day, List ids) {
        super.doby[day].remove(oldTime);
        super.doby[day].put(newTime, ids);
    }

}