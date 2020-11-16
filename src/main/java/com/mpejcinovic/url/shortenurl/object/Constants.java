package com.mpejcinovic.url.shortenurl.object;

/**
 * Class with constants for service.
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
public class Constants {

    public static final String DAILY_CRON_EXPRESSION = "0 0 8 ? * * *";
    public static final String WEEKLY_CRON_EXPRESSION = "0 1 8 ? * MON *";
    public static final String MONTHLY_CRON_EXPRESSION = "0 0 0 ? * 2#1 *";

    public static final String INFOBIP_URL = "infobip.com";
    public static final String HOST_HEADER = "Host";
    public static final String PROTOCOL = "http://";
}
