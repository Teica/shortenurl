package com.mpejcinovic.url.shortenurl.scheduler.daily;

import com.mpejcinovic.url.shortenurl.configuration.Config;
import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.db.DBHelper;
import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;
import com.mpejcinovic.url.shortenurl.object.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;

/**
 * A job for daily report. It retrieves
 * a number of requests submitted yesterday
 * and sends a report as an SMS.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Component
@DisallowConcurrentExecution
public class DailyReportJob implements Job {

    private final Object lock = new Object();

    @Autowired
    DBProperties dbProperties;

    @Autowired
    Config config;

    private static final Logger DAILY_REPORT_JOB_LOGGER = LogManager.getLogger(DailyReportJob.class);

    public DailyReportJob(){
    }

    /**
     * Retrieves a number of requests submitted yesterday
     * and initializes worker for sending a report via SMS.
     *
     * @param jobExecutionContext context for execution of a job
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        synchronized (lock) {

            DAILY_REPORT_JOB_LOGGER.info(MessageFormat.format("Cron expression for daily report: {0}", Constants.DAILY_CRON_EXPRESSION));

            DBHelper dbHelper = new DBHelper(dbProperties);
            int count = dbHelper.getNumberOfRequestForPreviousDay(LocalDate.now().minusDays(1));

            DailyReportWorker dailyReportWorker = new DailyReportWorker(count);
            dailyReportWorker.sendDailyReport(config.getInfobipSMSEndpoint(), config.getInfobipAccountKey());
        }
    }
}
