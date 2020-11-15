package com.mpejcinovic.url.shortenurl.scheduler.daily;

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
import java.time.LocalDate;

@Component
@DisallowConcurrentExecution
public class DailyReportJob implements Job {

    private final Object lock = new Object();

    @Autowired
    DBProperties dbProperties;

    @Autowired
    Config config;

    public DailyReportJob(){
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        synchronized (lock) {

            System.out.println(MessageFormat.format("Cron expression for daily report: {0}", Constants.DAILY_CRON_EXPRESSION));

            DBHelper dbHelper = new DBHelper(dbProperties);
            int count = dbHelper.getNumberOfRequestForPreviousDay(LocalDate.now().minusDays(1));

            DailyReportWorker dailyReportWorker = new DailyReportWorker(count);
            dailyReportWorker.sendDailyReport(config.getInfobipSMSEndpoint(), config.getInfobipAccountKey());
        }
    }
}
