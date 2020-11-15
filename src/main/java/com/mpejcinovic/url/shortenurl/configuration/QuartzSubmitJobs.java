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

@Configuration
public class QuartzSubmitJobs {
    private static final String CRON_EVERY_FIVE_MINUTES = "0 0/5 * ? * * *";

    @Bean(name = "dailyReport")
    public JobDetailFactoryBean jobDailyReport() {
        return createJobDetail(DailyReportJob.class, "Daily Report Job");
    }

    @Bean(name = "dailyReportTrigger")
    public CronTriggerFactoryBean triggerDailyReport(@Qualifier("dailyReport") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, Constants.DAILY_CRON_EXPRESSION, "Daily Report Trigger");
    }

    @Bean(name = "weeklyReport")
    public JobDetailFactoryBean jobWeeklyReport() {
        return createJobDetail(WeeklyReportJob.class, "Weekly Report Job");
    }

    @Bean(name = "weeklyReportTrigger")
    public CronTriggerFactoryBean triggerWeeklyReport(@Qualifier("weeklyReport") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, Constants.WEEKLY_CRON_EXPRESSION, "Weekly Report Trigger");
    }

    @Bean(name = "monthlyReport")
    public JobDetailFactoryBean jobMonthlyReport() {
        return createJobDetail(MonthlyReportJob.class, "Monthly Report Job");
    }

    @Bean(name = "monthlyReportTrigger")
    public CronTriggerFactoryBean triggerMonthlyReport(@Qualifier("monthlyReport") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, Constants.MONTHLY_CRON_EXPRESSION, "Monthly Report Trigger");
    }
}
