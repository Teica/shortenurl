package com.mpejcinovic.url.shortenurl.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Helper for dates.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 16.11.2020.
 */
public class DateHelper {

    /**
     * Retrieves start date for a previous week (MONDAY)
     *
     * @return a LocalDate object
     */
    public static LocalDate getPreviousWeekStartDate() {
        return LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * Retrieves end date for a previous date (SUNDAY)
     *
     * @return a LocalDate object
     */
    public static LocalDate getPreviousWeekLastDate() {
        return LocalDate.now().minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    /**
     * Retrieves first day in a previous month
     *
     * @return a LocalDate object
     */
    public static LocalDate getPreviousMonthStartDate() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Retrieves last day in a previous month
     *
     * @return a LocalDate object
     */
    public static LocalDate getPreviousMonthLastDate() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }

}
