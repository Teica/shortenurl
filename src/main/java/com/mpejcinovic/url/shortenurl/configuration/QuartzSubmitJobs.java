package com.mpejcinovic.url.shortenurl.configuration;

import com.mpejcinovic.url.shortenurl.object.Constants;
import com.mpejcinovic.url.shortenurl.scheduler.daily.DailyReportJob;
import com.mpejcinovic.url.shortenurl.scheduler.monthly.MonthlyReportJob;
import com.mpejcinovic.url.shortenurl.scheduler.weekly.WeeklyReportJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import static com.mpejcinovic.url.shortenurl.configuration.QuartzConfig.createCronTrigger;
import static com.mpejcinovic.url.shortenurl.configuration.QuartzConfig.createJobDetail;


/**
 * This class creates all required jobs and triggers.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Configuration
public class QuartzSubmitJobs {

    /**
     * Job for daily report.
     *
     * @return job detail for daily report
     */
    @Bean(name = "dailyReport")
    public JobDetailFactoryBean jobDailyReport() {
        return createJobDetail(DailyReportJob.class, "Daily Report Job");
    }

    /**
     * Trigger for daily report.
     *
     * @param jobDetail for daily report
     * @return a CRON trigger for daily report
     */
    @Bean(name = "dailyReportTrigger")
    public CronTriggerFactoryBean triggerDailyReport(@Qualifier("dailyReport") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, Constants.DAILY_CRON_EXPRESSION, "Daily Report Trigger");
    }

    /**
     * Job for weekly report.
     *
     * @return job detail for weekly report
     */
    @Bean(name = "weeklyReport")
    public JobDetailFactoryBean jobWeeklyReport() {
        return createJobDetail(WeeklyReportJob.class, "Weekly Report Job");
    }

    /**
     * Trigger for weekly report.
     *
     * @param jobDetail for weekly report
     * @return a CRON trigger for weekly report
     */
    @Bean(name = "weeklyReportTrigger")
    public CronTriggerFactoryBean triggerWeeklyReport(@Qualifier("weeklyReport") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, Constants.WEEKLY_CRON_EXPRESSION, "Weekly Report Trigger");
    }

    /**
     * Job for monthly report.
     *
     * @return job detail for monthly report
     */
    @Bean(name = "monthlyReport")
    public JobDetailFactoryBean jobMonthlyReport() {
        return createJobDetail(MonthlyReportJob.class, "Monthly Report Job");
    }

    /**
     * Trigger for monthly report.
     *
     * @param jobDetail for monthly report
     * @return a CRON trigger for monthly report
     */
    @Bean(name = "monthlyReportTrigger")
    public CronTriggerFactoryBean triggerMonthlyReport(@Qualifier("monthlyReport") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, Constants.MONTHLY_CRON_EXPRESSION, "Monthly Report Trigger");
    }
}
