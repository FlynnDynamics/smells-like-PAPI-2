package com.smell.application.structure;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

/**
 * Provides utility methods to compare date and time ranges for different entities in the system.
 * This service is used to determine whether two given time periods overlap using precise date-time parsing.
 * It supports handling 'now' as a dynamic reference to the current time in UTC for comparisons.
 *
 * The service logs detailed information about parsing errors and operational steps to facilitate debugging and ensure reliable operations.
 *
 * @author FlynnDynamics
 * @version 0.x
 * @since 24/04/24
 */
public class ComparisonService {
    private static final Logger LOGGER = Logger.getLogger(ComparisonService.class.getName());

    /**
     * Determines whether two time periods overlap.
     * Each time period is defined by a start and an end datetime string in ISO_OFFSET_DATE_TIME format.
     * This method parses the datetime strings and checks if the two periods overlap.
     *
     * @param startA The start datetime of the first period.
     * @param endA The end datetime of the first period.
     * @param startB The start datetime of the second period.
     * @param endB The end datetime of the second period.
     * @return true if the periods overlap, false otherwise.
     * @throws DateTimeParseException if any datetime string cannot be parsed correctly, indicating a potential input error.
     */
    public static boolean isTimeWithinRange(String startA, String endA, String startB, String endB) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        LocalDateTime startADate, endADate, startBDate, endBDate;
        try {
            startADate = parseDate(startA, formatter);
            endADate = parseDate(endA, formatter);
            startBDate = parseDate(startB, formatter);
            endBDate = parseDate(endB, formatter);
        } catch (DateTimeParseException e) {
            LOGGER.severe("Error parsing dates: " + e.getMessage());
            throw new DateTimeParseException("Null date found during comparison", "", 0);
        }

        if (startADate == null || endADate == null || startBDate == null || endBDate == null) {
            LOGGER.severe("One or more date inputs are null after parsing, cannot perform comparison.");
            throw new DateTimeParseException("Null date found during comparison", "", 0);
        }

        return !startADate.isAfter(endBDate) && !endADate.isBefore(startBDate);
    }

    /**
     * Parses a datetime string into a LocalDateTime object.
     * Handles the special case where 'now' is provided, returning the current datetime in UTC.
     * If parsing fails, logs the error and returns null to indicate the failure.
     *
     * @param time The datetime string to parse, which can be an ISO_OFFSET_DATE_TIME formatted string or 'now'.
     * @param formatter The DateTimeFormatter to use, typically ISO_OFFSET_DATE_TIME.
     * @return The parsed LocalDateTime, or null if parsing fails.
     */
    private static LocalDateTime parseDate(String time, DateTimeFormatter formatter) {
        if ("now".equals(time)) {
            return LocalDateTime.now(ZoneOffset.UTC);
        }

        try {
            return LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            LOGGER.warning("Failed to parse date string: " + time + "; error: " + e.getMessage());
            return null; // Return null to indicate parsing failure.
        }
    }

    /**
     * Retrieves the current UTC time as a formatted string in ISO_OFFSET_DATE_TIME format.
     * Logs the current time to assist with debugging or logging time-sensitive operations.
     *
     * @return The current UTC time as a string formatted according to ISO_OFFSET_DATE_TIME.
     */
    public static String getCurrentUTCTime() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String formattedTime = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LOGGER.info("Current UTC time: " + formattedTime);
        return formattedTime;
    }
}
