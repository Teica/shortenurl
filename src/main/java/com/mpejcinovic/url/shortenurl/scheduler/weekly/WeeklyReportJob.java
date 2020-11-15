package com.mpejcinovic.url.shortenurl.scheduler.weekly;

import com.mpejcinovic.url.shortenurl.configuration.Config;
import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.db.DBHelper;
import com.mpejcinovic.url.shortenurl.object.Constants;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Component
@DisallowConcurrentExecution
public class WeeklyReportJob implements Job {

    private final Object lock = new Object();

    @Autowired
    DBProperties dbProperties;

    @Autowired
    Config config;

    public WeeklyReportJob(){
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        synchronized (lock) {

            System.out.println(MessageFormat.format("Cron expression for weekly report: {0}", Constants.WEEKLY_CRON_EXPRESSION));

            DBHelper dbHelper = new DBHelper(dbProperties);
            int count = dbHelper.getNumberOfRequestForDateRange(
                    getPreviousWeekStartDate(),
                    getPreviousWeekLastDate()
            );

            WeeklyReportWorker weeklyReportWorker = new WeeklyReportWorker(count);
            weeklyReportWorker.sendWeeklyReport(config.getInfobipSMSEndpoint(), config.getInfobipAccountKey());
        }
    }

    public LocalDate getPreviousWeekStartDate() {
       return LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public LocalDate getPreviousWeekLastDate() {
        return LocalDate.now().minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }
}
