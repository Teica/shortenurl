package com.mpejcinovic.url.shortenurl.scheduler.monthly;

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
import java.time.temporal.TemporalAdjusters;

@Component
@DisallowConcurrentExecution
public class MonthlyReportJob implements Job {

    private final Object lock = new Object();

    @Autowired
    DBProperties dbProperties;

    @Autowired
    Config config;

    public MonthlyReportJob(){
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        synchronized (lock) {

            System.out.println(MessageFormat.format("Cron expression for monthly report: {0}", Constants.MONTHLY_CRON_EXPRESSION));

            DBHelper dbHelper = new DBHelper(dbProperties);
            int count = dbHelper.getNumberOfRequestForDateRange(
                    getPreviousMonthStartDate(),
                    getPreviousMonthLastDate()
            );

            MonthlyReportWorker monthlyReportWorker = new MonthlyReportWorker(count);
            monthlyReportWorker.sendMonthlyReport(config.getInfobipSMSEndpoint(), config.getInfobipAccountKey());
        }
    }

    public LocalDate getPreviousMonthStartDate() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
    }

    public LocalDate getPreviousMonthLastDate() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }
}
