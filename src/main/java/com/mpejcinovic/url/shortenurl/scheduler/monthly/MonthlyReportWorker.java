package com.mpejcinovic.url.shortenurl.scheduler.monthly;

import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that handles methods for monthly reports.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
public class MonthlyReportWorker {

    private int count;

    private static final Logger MONTHLY_REPORT_WORKER_LOGGER = LogManager.getLogger(MonthlyReportWorker.class);

    MonthlyReportWorker(int count){
        this.count = count;
    }

    /**
     * Sends monthly report to via SMS.
     *
     * @param endpoint an endpoint for sending a monthly report
     * @param key a key for an API
     */
    public void sendMonthlyReport(String endpoint, String key) {
        MONTHLY_REPORT_WORKER_LOGGER.debug("Method sendMonthlyReport started");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Number of requests for previous month is ").append(count);

        InfobipSMSClient infobipSMSClient = new InfobipSMSClient(stringBuilder.toString(), endpoint, key);
        String response = infobipSMSClient.sendSMS();

        MONTHLY_REPORT_WORKER_LOGGER.info(response);

        MONTHLY_REPORT_WORKER_LOGGER.debug("Method sendMonthlyReport end");
    }
}
