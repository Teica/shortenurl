package com.mpejcinovic.url.shortenurl.scheduler.weekly;

import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that handles methods for weekly reports.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
public class WeeklyReportWorker {

    private int count;

    private static final Logger WEEKLY_REPORT_WORKER_LOGGER = LogManager.getLogger(WeeklyReportWorker.class);

    WeeklyReportWorker(int count){
        this.count = count;
    }

    /**
     * Sends weekly report to via SMS.
     *
     * @param endpoint an endpoint for sending a weekly report
     * @param key a key for an API
     */
    public void sendWeeklyReport(String endpoint, String key) {
        WEEKLY_REPORT_WORKER_LOGGER.debug("Method sendWeeklyReport started");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Number of requests for previous week is ").append(count);

        InfobipSMSClient infobipSMSClient = new InfobipSMSClient(stringBuilder.toString(), endpoint, key);
        String response = infobipSMSClient.sendSMS();

        WEEKLY_REPORT_WORKER_LOGGER.info(response);

        WEEKLY_REPORT_WORKER_LOGGER.debug("Method sendWeeklyReport end");
    }
}