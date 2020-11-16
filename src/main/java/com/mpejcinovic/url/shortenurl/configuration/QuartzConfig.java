package com.mpejcinovic.url.shortenurl.configuration;

import com.mpejcinovic.url.shortenurl.controllers.StatusController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Properties;

/**
 * Provides configuration for Quartz scheduler.
 *
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Slf4j
@Configuration
public class QuartzConfig {
    private ApplicationContext applicationContext;
    private DataSource dataSource;

    private static final Logger QUARTZ_CONFIG_LOGGER = LogManager.getLogger(QuartzConfig.class);


    /**
     * Constructor for QuartzConfig.
     *
     * @param applicationContext an application context
     * @param dataSource a datasource
     */
    public QuartzConfig(ApplicationContext applicationContext, DataSource dataSource) {
        this.applicationContext = applicationContext;
        this.dataSource = dataSource;
    }

    /**
     * Creates a job factory.
     *
     * @return a job factory
     */
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * Configures a scheduler.
     *
     * @param triggers triggers to be set to a scheduler factory
     * @return a scheduler factory
     */
    @Bean
    public SchedulerFactoryBean scheduler(Trigger... triggers) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        Properties properties = new Properties();

        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);

        if (ArrayUtils.isNotEmpty(triggers)) {
            schedulerFactory.setTriggers(triggers);
        }

        return schedulerFactory;
    }

    /**
     * Creates a trigger based on provided parameters.
     *
     * @param jobDetail details regarding a job
     * @param pollFrequencyMs frequency expressed in milliseconds
     * @param triggerName a name of a trigger
     * @return a trigger
     */
    static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs, String triggerName) {
        QUARTZ_CONFIG_LOGGER.debug("createTrigger(jobDetail={}" + jobDetail.toString() +", pollFrequencyMs=" + pollFrequencyMs + ", triggerName=" + triggerName + ")");

        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setName(triggerName);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);

        return factoryBean;
    }

    /**
     * Creates a trigger based on a CRON expression.
     *
     * @param jobDetail details regarding a job
     * @param cronExpression an expression for a frequency of a starting a job
     * @param triggerName a name of a trigger
     * @return a CRON trigger
     */
    static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression, String triggerName) {
        QUARTZ_CONFIG_LOGGER.debug("createCronTrigger(jobDetail" + jobDetail.toString() +", cronExpression=" + cronExpression + ", triggerName=" + triggerName + ")");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setStartTime(calendar.getTime());
        factoryBean.setStartDelay(0L);
        factoryBean.setName(triggerName);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

        return factoryBean;
    }

    /**
     * Creates job detail.
     *
     * @param jobClass a class representing a job
     * @param jobName a name of a job
     * @return job detail object
     */
    static JobDetailFactoryBean createJobDetail(Class jobClass, String jobName) {
        QUARTZ_CONFIG_LOGGER.debug("createJobDetail(jobClass=" + jobClass.getName() + ", jobName=" + jobName + ")");

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);

        return factoryBean;
    }
}
