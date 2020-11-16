package com.mpejcinovic.url.shortenurl.scheduler.weekly;

import com.mpejcinovic.url.shortenurl.configuration.Config;
import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.db.DBHelper;
import com.mpejcinovic.url.shortenurl.object.Constants;
import com.mpejcinovic.url.shortenurl.scheduler.monthly.MonthlyReportWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static com.mpejcinovic.url.shortenurl.helpers.DateHelper.getPreviousWeekLastDate;
import static com.mpejcinovic.url.shortenurl.helpers.DateHelper.getPreviousWeekStartDate;

/**
 * A job for weekly report. It retrieves
 * a number of requests submitted during
 * previous week and sends a report as
 * an SMS.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Component
@DisallowConcurrentExecution
public class WeeklyReportJob implements Job {

    private final Object lock = new Object();

    private static final Logger WEEKLY_REPORT_JOB_LOGGER = LogManager.getLogger(WeeklyReportJob.class);

    @Autowired
    DBProperties dbProperties;

    @Autowired
    Config config;

    public WeeklyReportJob(){
    }

    /**
     * Retrieves a number of requests submitted during previous week
     * and initializes worker for sending a report via SMS.
     *
     * @param jobExecutionContext context for execution of a job
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        synchronized (lock) {

            WEEKLY_REPORT_JOB_LOGGER.info(MessageFormat.format("Cron expression for weekly report: {0}", Constants.WEEKLY_CRON_EXPRESSION));

            DBHelper dbHelper = new DBHelper(dbProperties);
            int count = dbHelper.getNumberOfRequestForDateRange(
                    getPreviousWeekStartDate(),
                    getPreviousWeekLastDate()
            );

            WeeklyReportWorker weeklyReportWorker = new WeeklyReportWorker(count);
            weeklyReportWorker.sendWeeklyReport(config.getInfobipSMSEndpoint(), config.getInfobipAccountKey());
        }
    }

}
