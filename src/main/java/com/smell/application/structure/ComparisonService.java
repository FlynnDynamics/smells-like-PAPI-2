package com.smell.application.structure;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ComparisonService {

    /**
     * Checks if two time periods overlap.
     *
     * @param startA Start date of the first period, formatted as ISO_OFFSET_DATE_TIME.
     * @param endA   End date of the first period, formatted as ISO_OFFSET_DATE_TIME.
     * @param startB Start date of the second period, formatted as ISO_OFFSET_DATE_TIME.
     * @param endB   End date of the second period, formatted as ISO_OFFSET_DATE_TIME.
     * @return true if the periods overlap, false otherwise.
     */
    public static boolean isTimeWithinRange(String startA, String endA, String startB, String endB) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        LocalDateTime startADate = parseDate(startA, formatter);
        LocalDateTime endADate = parseDate(endA, formatter);
        LocalDateTime startBDate = parseDate(startB, formatter);
        LocalDateTime endBDate = parseDate(endB, formatter);

        return !startADate.isAfter(endBDate) && !endADate.isBefore(startBDate);
    }

    /**
     * Parses a date string or returns the current UTC time if the input is "now".
     *
     * @param time     The time string to parse.
     * @param formatter The DateTimeFormatter to use.
     * @return A LocalDateTime object.
     */
    private static LocalDateTime parseDate(String time, DateTimeFormatter formatter) {
        if (time.equals("now")) {
            return LocalDateTime.now(ZoneOffset.UTC);
        } else {
            return LocalDateTime.parse(time, formatter);
        }
    }

    /**
     * Returns the current UTC time in ISO_OFFSET_DATE_TIME format.
     *
     * @return String representing the current UTC time in "yyyy-MM-dd'T'HH:mm:ssZ" format.
     */
    public static String getCurrentUTCTime() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        return now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
