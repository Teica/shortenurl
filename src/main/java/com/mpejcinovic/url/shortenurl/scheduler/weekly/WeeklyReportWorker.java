package com.mpejcinovic.url.shortenurl.scheduler.weekly;

import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;

public class WeeklyReportWorker {

    private int count;
    WeeklyReportWorker(int count){
        this.count = count;
    }

    public void sendWeeklyReport(String endpoint, String key) {
        System.out.println("Method sendWeeklyReport started");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Number of requests for previous week is ").append(count);

        InfobipSMSClient infobipSMSClient = new InfobipSMSClient(stringBuilder.toString(), endpoint, key);
        String response = infobipSMSClient.sendSMS();

        System.out.println(response);

        System.out.println("Method sendWeeklyReport end");
    }
}