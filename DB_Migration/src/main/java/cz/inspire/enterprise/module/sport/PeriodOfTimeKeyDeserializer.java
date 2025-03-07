package cz.inspire.enterprise.module.sport;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import cz.inspire.common.util.DayOfYear;
import cz.inspire.common.util.PeriodOfTime;
import cz.inspire.common.util.TimeOfDay;
import cz.inspire.common.util.TimeOfWeek;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PeriodOfTimeKeyDeserializer extends KeyDeserializer {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        if (key.contains(" | ")) { // Case for PeriodOfTimeInDay
            String[] parts = key.split(" \\| ", 2);
            if (parts.length != 2) {
                throw new IOException("Invalid PeriodOfTimeInDay key format: " + key);
            }

            try {
                Date date = dateFormat.parse(parts[0]);
                PeriodOfTime period = parsePeriodOfTime(parts[1]);
                return new PeriodOfTimeInDay(date, period);
            } catch (ParseException e) {
                throw new IOException("Error parsing date in PeriodOfTimeInDay key: " + parts[0], e);
            }
        } else {
            return parsePeriodOfTime(key);
        }
    }

    private PeriodOfTime parsePeriodOfTime(String key) throws IOException {
        String[] parts = key.split(" - ");
        if (parts.length != 2) {
            throw new IOException("Invalid PeriodOfTime key format: " + key);
        }

        MomentOfTime start = parseMomentOfTime(parts[0]);
        MomentOfTime end = parseMomentOfTime(parts[1]);

        return new PeriodOfTime(start, end);
    }

    private MomentOfTime parseMomentOfTime(String timeString) throws IOException {
        if (timeString.contains(":")) {
            return parseTimeOfDay(timeString);
        } else if (timeString.contains("--")) {
            return parseTimeOfWeek(timeString);
        } else if (timeString.contains("-")) {
            return parseDayOfYear(timeString);
        } else {
            throw new IOException("Unknown MomentOfTime format: " + timeString);
        }
    }

    private TimeOfDay parseTimeOfDay(String time) {
        String[] split = time.split(":");
        return new TimeOfDay(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    private TimeOfWeek parseTimeOfWeek(String time) {
        String[] split = time.split(" -- ");
        int dayOfWeek = Integer.parseInt(split[0]);
        TimeOfDay timeOfDay = parseTimeOfDay(split[1]);
        return new TimeOfWeek(dayOfWeek, timeOfDay);
    }

    private DayOfYear parseDayOfYear(String date) {
        String[] dateTimeSplit = date.split(" ");
        String[] dateSplit = dateTimeSplit[0].split("-");
        String[] timeSplit = dateTimeSplit[1].split(":");

        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        return new DayOfYear(year, month, day, hour, minute);
    }
}
