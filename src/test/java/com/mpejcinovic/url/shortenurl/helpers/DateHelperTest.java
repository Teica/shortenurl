package com.mpejcinovic.url.shortenurl.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DateHelperTest {

    @Test
    void getPreviousWeekStartDate_DayIsSuccessfullyRetrieved() {
        System.out.println("-> getPreviousWeekStartDate_DayIsSuccessfullyRetrieved executing...");

        LocalDate previousMonday = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Assertions.assertEquals(previousMonday, DateHelper.getPreviousWeekStartDate());
    }

    @Test
    void getPreviousWeekLastDate_DayIsSuccessfullyRetrieved() {
        System.out.println("-> getPreviousWeekLastDate_DayIsSuccessfullyRetrieved executing...");

        LocalDate previousSunday = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Assertions.assertEquals(previousSunday, DateHelper.getPreviousWeekLastDate());
    }

    @Test
    void getPreviousMonthStartDate_DayIsSuccessfullyRetrieved() {
        System.out.println("-> getPreviousMonthStartDate_DayIsSuccessfullyRetrieved executing...");

        LocalDate firstDayLastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        Assertions.assertEquals(firstDayLastMonth, DateHelper.getPreviousMonthStartDate());
    }

    @Test
    void getPreviousMonthLastDate_DayIsSuccessfullyRetrieved() {
        System.out.println("-> getPreviousMonthLastDate_DayIsSuccessfullyRetrieved executing...");

        LocalDate lastDayLastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        Assertions.assertEquals(lastDayLastMonth, DateHelper.getPreviousMonthLastDate());
    }

}
