package cz.inspire.sport.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.inspire.utils.PeriodOfTime;
import cz.inspire.utils.PeriodOfTimeInDay;

import java.io.IOException;
import java.text.SimpleDateFormat;


public class PeriodOfTimeKeySerializer extends JsonSerializer<Object> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof PeriodOfTimeInDay periodInDay) {
            String dateString = dateFormat.format(periodInDay.getDate());
            gen.writeFieldName(dateString + " | " + periodInDay.getPeriodOfTime().toString());
        } else if (value instanceof PeriodOfTime period) {
            gen.writeFieldName(period.toString());
        } else {
            throw new IOException("Unsupported key type: " + value.getClass().getName());
        }
    }
}

