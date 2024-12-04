package cz.inspire.thesis.data.utils;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the opening hours of an object.
 * Generated to for testing purposes of OtviraciDoba Entity ...
 */
public class OtviraciDoba implements Serializable {

    // Map of days (1 = Monday, 7 = Sunday) to open and close times
    private final Map<Integer, TimeRange> openingHours = new HashMap<>();

    /**
     * Represents a time range with a start and end time.
     */
    public static class TimeRange {
        private LocalTime open;
        private LocalTime close;

        public TimeRange() {}

        public TimeRange(LocalTime open, LocalTime close) {
            this.open = open;
            this.close = close;
        }

        public LocalTime getOpen() {
            return open;
        }

        public void setOpen(LocalTime open) {
            this.open = open;
        }

        public LocalTime getClose() {
            return close;
        }

        public void setClose(LocalTime close) {
            this.close = close;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeRange timeRange = (TimeRange) o;
            return Objects.equals(open, timeRange.open) && Objects.equals(close, timeRange.close);
        }

        @Override
        public int hashCode() {
            return Objects.hash(open, close);
        }

        @Override
        public String toString() {
            return "TimeRange{" +
                    "open=" + open +
                    ", close=" + close +
                    '}';
        }
    }

    /**
     * Sets the opening and closing times for a specific day.
     *
     * @param day   Day of the week (1 = Monday, 7 = Sunday).
     * @param open  Opening time.
     * @param close Closing time.
     */
    public void setOpeningHours(int day, LocalTime open, LocalTime close) {
        if (day < 1 || day > 7) {
            throw new IllegalArgumentException("Day must be between 1 (Monday) and 7 (Sunday).");
        }
        openingHours.put(day, new TimeRange(open, close));
    }

    /**
     * Gets the opening hours for a specific day.
     *
     * @param day Day of the week (1 = Monday, 7 = Sunday).
     * @return The opening and closing times, or null if not set.
     */
    public TimeRange getOpeningHours(int day) {
        return openingHours.get(day);
    }

    /**
     * Checks if the object is open at a given day and time.
     *
     * @param day  Day of the week (1 = Monday, 7 = Sunday).
     * @param time The time to check.
     * @return True if the object is open, false otherwise.
     */
    public boolean isOpen(int day, LocalTime time) {
        TimeRange range = openingHours.get(day);
        return range != null && !time.isBefore(range.getOpen()) && !time.isAfter(range.getClose());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OtviraciDoba that = (OtviraciDoba) o;
        return Objects.equals(openingHours, that.openingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openingHours);
    }

    @Override
    public String toString() {
        return "OtviraciDoba{" +
                "openingHours=" + openingHours +
                '}';
    }
}
