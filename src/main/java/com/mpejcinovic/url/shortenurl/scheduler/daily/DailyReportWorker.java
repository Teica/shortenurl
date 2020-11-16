package com.mpejcinovic.url.shortenurl.scheduler.daily;

import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class that handles methods for daily reports.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
public class DailyReportWorker {

    private int count;

    private static final Logger DAILY_REPORT_WORKER_LOGGER = LogManager.getLogger(DailyReportWorker.class);

    DailyReportWorker(int count){
        this.count = count;
    }

    /**
     * Sends daily report to via SMS.
     *
     * @param endpoint an endpoint for sending a daily report
     * @param key a key for an API
     */
    public void sendDailyReport(String endpoint, String key) {
        DAILY_REPORT_WORKER_LOGGER.debug("Method sendDailyReport started");

        LocalDate localDate = LocalDate.now().minusDays(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Number of requests on ");
        stringBuilder.append(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        stringBuilder.append(" is: ").append(count); //text za message

        InfobipSMSClient infobipSMSClient = new InfobipSMSClient(stringBuilder.toString(), endpoint, key);
        String response = infobipSMSClient.sendSMS();

        DAILY_REPORT_WORKER_LOGGER.info(response);

        DAILY_REPORT_WORKER_LOGGER.debug("Method sendDailyReport end");
    }
}
