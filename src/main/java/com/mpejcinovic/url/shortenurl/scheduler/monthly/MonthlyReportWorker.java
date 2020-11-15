package com.mpejcinovic.url.shortenurl.scheduler.monthly;

import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;

public class MonthlyReportWorker {

    private int count;
    MonthlyReportWorker(int count){
        this.count = count;
    }

    public void sendMonthlyReport(String endpoint, String key) {
        System.out.println("Method sendMonthlyReport started");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Number of requests for previous month is ").append(count);

        InfobipSMSClient infobipSMSClient = new InfobipSMSClient(stringBuilder.toString(), endpoint, key);
        String response = infobipSMSClient.sendSMS();

        System.out.println(response);

        System.out.println("Method sendMonthlyReport end");
    }
}
