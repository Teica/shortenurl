package com.mpejcinovic.url.shortenurl.scheduler.daily;

import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyReportWorker {

    private int count;
    DailyReportWorker(int count){
        this.count = count;
    }

    public void sendDailyReport(String endpoint, String key) {
        System.out.println("Method sendDailyReport started");

        LocalDate localDate = LocalDate.now().minusDays(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Number of requests on ");
        stringBuilder.append(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        stringBuilder.append(" is: ").append(count); //text za message

        InfobipSMSClient infobipSMSClient = new InfobipSMSClient(stringBuilder.toString(), endpoint, key);
        String response = infobipSMSClient.sendSMS();

        System.out.println(response);

        System.out.println("Method sendDailyReport end");
    }
}
