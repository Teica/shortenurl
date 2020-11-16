package com.mpejcinovic.url.shortenurl.helpers;

import com.mpejcinovic.url.shortenurl.db.DBHelper;
import com.mpejcinovic.url.shortenurl.integration.InfobipSMSClient;
import com.mpejcinovic.url.shortenurl.object.Constants;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Helper for URLs.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 14.11.2020.
 */
public class UrlHelper {

    private static final Logger URL_HELPER_LOGGER = LogManager.getLogger(UrlHelper.class);
    
    /**
     * Validates a URL
     *
     * @param url a URL for validation
     * @return an indicator is a URL valid or not
     */
    public static boolean isUrlValid(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    /**
     * Shortens URL by converting an id to a Base 62.
     *
     * @param id an identifier for a newly shortened URL
     * @return a converted value (hash)
     */
    public static String shortenUrl(int id) {

        StringBuilder chars = new StringBuilder();

        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = LongStream.rangeClosed(0, 9)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        chars.append(lowercase).append(lowercase.toUpperCase()).append(numbers);

        char[] urlCharacters = chars.toString().toCharArray();

        StringBuffer shortenedUrl = new StringBuffer();

        URL_HELPER_LOGGER.debug("ID for shortening: " + id);
        while (id > 0) {
            shortenedUrl.append(urlCharacters[id % 62]);
            id = id / 62;
        }

        URL_HELPER_LOGGER.debug("shortenedUrl: " + shortenedUrl.reverse().toString());

        return shortenedUrl.reverse().toString();
    }

    /**
     * Reverses hash to an identifier.
     *
     * @param shortURL a hash for shortened URL
     * @return an identifier
     */
    public static int shortURLtoID(String shortURL) {
        int id = 0;
        for (int i = 0; i < shortURL.length(); i++) {
            if ('a' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'z')
                id = id * 62 + shortURL.charAt(i) - 'a';
            if ('A' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'Z')
                id = id * 62 + shortURL.charAt(i) - 'A' + 26;
            if ('0' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= '9')
                id = id * 62 + shortURL.charAt(i) - '0' + 52;
        }
        return id;
    }

    /**
     * Prepares a URL for inspection.
     *
     * @param url a user provided URL
     * @return transformed URL
     */
    public static String prepareUrl(String url) {
        URL_HELPER_LOGGER.debug("Method prepareUrl started!");

        int index = url.indexOf("://");
        url = url.substring(index, url.length());
        URL_HELPER_LOGGER.debug("Prepared URL: " + url);

        URL_HELPER_LOGGER.debug("Method prepareUrl finished!");
        return url;
    }

    /**
     * Checks if it is an Infobip URL provided
     *
     * @param url
     * @return true if it is an Infobip URL, false otherwise
     */
    public static boolean isInfobipUrl(String url) {
        return url.indexOf(Constants.INFOBIP_URL) > 0 ? true : false;
    }

    /**
     * Sends statistics (daily, weekly, monthly)
     * using Infobip SMS endpoint.
     *
     * @param endpoint an Infobip endpoint
     * @param key a key for Infobip API
     * @param dailyCount a number of requests for yesterday
     * @param weeklyCount a number of requests in a previous week
     * @param monthlyCount a number of requests in a previous month
     */
    public static void sendStatistics(String endpoint, String key, int dailyCount, int weeklyCount, int monthlyCount) {
        URL_HELPER_LOGGER.debug("Method sendStatistics started!");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Daily count: ").append(dailyCount).append(", ");
        stringBuilder.append("weekly count: ").append(weeklyCount).append(", ");
        stringBuilder.append("monthly count: ").append(monthlyCount);

        InfobipSMSClient infobipSMSClient = new InfobipSMSClient(stringBuilder.toString(), endpoint, key);
        String response = infobipSMSClient.sendSMS();
        URL_HELPER_LOGGER.info(response);

        URL_HELPER_LOGGER.debug("Method sendStatistics finished!");
    }
}
