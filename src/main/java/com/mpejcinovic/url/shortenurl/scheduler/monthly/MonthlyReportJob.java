package com.mpejcinovic.url.shortenurl.scheduler.monthly;

import com.mpejcinovic.url.shortenurl.configuration.Config;
import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.db.DBHelper;
import com.mpejcinovic.url.shortenurl.object.Constants;
import com.mpejcinovic.url.shortenurl.scheduler.daily.DailyReportWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static com.mpejcinovic.url.shortenurl.helpers.DateHelper.getPreviousMonthLastDate;
import static com.mpejcinovic.url.shortenurl.helpers.DateHelper.getPreviousMonthStartDate;

/**
 * A job for monthly report. It retrieves
 * a number of requests submitted during
 * previous month and sends a report as
 * an SMS.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Component
@DisallowConcurrentExecution
public class MonthlyReportJob implements Job {

    private final Object lock = new Object();

    private static final Logger MONTHLY_REPORT_JOB_LOGGER = LogManager.getLogger(MonthlyReportJob.class);

    @Autowired
    DBProperties dbProperties;

    @Autowired
    Config config;

    public MonthlyReportJob(){
    }

    /**
     * Retrieves a number of requests submitted during previous month
     * and initializes worker for sending a report via SMS.
     *
     * @param jobExecutionContext context for execution of a job
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        synchronized (lock) {

            MONTHLY_REPORT_JOB_LOGGER.info(MessageFormat.format("Cron expression for monthly report: {0}", Constants.MONTHLY_CRON_EXPRESSION));

            DBHelper dbHelper = new DBHelper(dbProperties);
            int count = dbHelper.getNumberOfRequestForDateRange(
                    getPreviousMonthStartDate(),
                    getPreviousMonthLastDate()
            );

            MonthlyReportWorker monthlyReportWorker = new MonthlyReportWorker(count);
            monthlyReportWorker.sendMonthlyReport(config.getInfobipSMSEndpoint(), config.getInfobipAccountKey());
        }
    }
}
