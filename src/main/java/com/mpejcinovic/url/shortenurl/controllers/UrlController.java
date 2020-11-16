package com.mpejcinovic.url.shortenurl.controllers;

import com.mpejcinovic.url.shortenurl.configuration.Config;
import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.db.DBHelper;
import com.mpejcinovic.url.shortenurl.helpers.DateHelper;
import com.mpejcinovic.url.shortenurl.helpers.UrlHelper;
import com.mpejcinovic.url.shortenurl.object.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mpejcinovic.url.shortenurl.object.Constants.HOST_HEADER;

/**
 * The URLController class handles the methods
 * for shortening URLs and reversing the shortened
 * URL to the initial one.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 13.11.2020.
 */
@RestController
public class UrlController {

    @Autowired
    private Config config;

    @Autowired
    private DBProperties dbProperties;

    private final AtomicInteger counter = new AtomicInteger(1);
    private static final Logger URL_LOGGER = LogManager.getLogger(UrlController.class);

    /**
     * Shortens a provided URL. It validates the entry,
     * checks if there is already a saved version of
     * shortened URL and returns if if there is such.
     * If there is no saved version of shortened URL
     * for provided entry, it will be prepared and returned.
     * If there an entry contains infobip.com, reports
     * will be sent.
     *
     * @param request           an HTTP request
     * @param shortenUrlRequest a request with an URL to be shortened
     * @return response with a shortened URL
     */
    @ResponseStatus
    @PostMapping(value = "/shorten", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity shortenUrl(HttpServletRequest request,
                                     @Valid @RequestBody ShortenUrlRequest shortenUrlRequest) {
        URL_LOGGER.info("Method shortenUrl started.");
        ShortenUrlResponse shortenUrlResponse = null;

        String trimmedUrl = shortenUrlRequest.getUrl().trim();
        DBHelper dbHelper = new DBHelper(dbProperties);

        if (!UrlHelper.isUrlValid(trimmedUrl)) {

            URL_LOGGER.debug(trimmedUrl + " is not valid!");

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setError(HttpStatus.BAD_REQUEST.name());
            errorResponse.setMessage(trimmedUrl + " is not valid!");
            errorResponse.setPath(request.getRequestURI());

            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }

        int urlLength = trimmedUrl.length();
        if (trimmedUrl.substring(urlLength - 1, urlLength).equals("/")) {
            trimmedUrl = trimmedUrl.substring(0, urlLength - 1);
        }

        String preparedUrl = UrlHelper.prepareUrl(trimmedUrl);

        Url existingUrl = dbHelper.getUrlByLongUrl(preparedUrl);

        if (existingUrl != null) {
            URL_LOGGER.debug("URL already inserted!");
            return new ResponseEntity(
                    new ShortenUrlResponse(existingUrl.getShortUrl()),
                    HttpStatus.OK);
        }

        Url lastUrl = dbHelper.getLastUrl();

        if (counter.get() == 1 && lastUrl != null) {
            int idx = lastUrl.getShortUrl().lastIndexOf('/');

            String hash = lastUrl.getShortUrl().substring(idx + 1, lastUrl.getShortUrl().length());
            int id = UrlHelper.shortURLtoID(hash);

            counter.set(id + 1);
        }
        String shortenUrl = UrlHelper.shortenUrl(counter.get());
        if (shortenUrl != null && shortenUrl.length() > 0) {
            shortenUrl = prepareUrlResponse(request, shortenUrl);

            Url url = new Url();
            url.setShortUrl(shortenUrl);
            url.setLongUrl(trimmedUrl);
            url.setSubmitDate(LocalDate.now());

            int id = dbHelper.insertUrl(url);
            counter.getAndIncrement();

            if (UrlHelper.isInfobipUrl(trimmedUrl)) {

                int dailyCount = dbHelper.getNumberOfRequestForPreviousDay(LocalDate.now().minusDays(-1));

                int weeklyCount = dbHelper.getNumberOfRequestForDateRange(
                        DateHelper.getPreviousWeekStartDate(),
                        DateHelper.getPreviousWeekLastDate()
                );

                int monthlyCount = dbHelper.getNumberOfRequestForDateRange(
                        DateHelper.getPreviousMonthStartDate(),
                        DateHelper.getPreviousMonthLastDate()
                );

                UrlHelper.sendStatistics(
                        config.getInfobipSMSEndpoint(),
                        config.getInfobipAccountKey(),
                        dailyCount,
                        weeklyCount,
                        monthlyCount);
            }

            shortenUrlResponse = new ShortenUrlResponse(shortenUrl);
            return new ResponseEntity(shortenUrlResponse, HttpStatus.OK);
        } else {
            URL_LOGGER.debug("Issue with Shorten URL: " + shortenUrl);

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            errorResponse.setMessage("Issue with Shorten URL: " + shortenUrl);
            errorResponse.setPath(request.getRequestURI());

            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Prepares a URL to be returned.
     *
     * @param request an HTTP request
     * @param url     value for a URL
     * @return a built URL
     */
    private String prepareUrlResponse(HttpServletRequest request, String url) {

        String requestUrl = request.getRequestURI();
        String context = requestUrl.substring(0, requestUrl.lastIndexOf("shorten") - 1);

        StringBuilder finalShortenedUrl = new StringBuilder();
        finalShortenedUrl.append(com.mpejcinovic.url.shortenurl.object.Constants.PROTOCOL).append(request.getHeader(HOST_HEADER)).append(context).append("/").append(url);

        URL_LOGGER.debug("Prepared URL: " + finalShortenedUrl);
        return finalShortenedUrl.toString();
    }

    /**
     * Reverses a shortened URL to its original form
     * and redirects to an original URL.
     *
     * @param hash                a hash representing shortened form of a URL
     * @return an entity with an original URL
     */
    @ResponseBody
    @GetMapping(value = "{hash}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOriginalLinkAndRedirect(HttpServletRequest request, @PathVariable String hash) {
        URL_LOGGER.info("Method getOriginalLinkAndRedirect started!");
        URL_LOGGER.debug("Hash: " + hash);
        DBHelper dbHelper = new DBHelper(dbProperties);

        int id = UrlHelper.shortURLtoID(hash);
        URL_LOGGER.debug("ID is: " + id);
        try {
            Url url = dbHelper.getUrlById(id);

            if (url == null) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setTimestamp(LocalDateTime.now().toString());
                errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                errorResponse.setError(HttpStatus.BAD_REQUEST.name());
                errorResponse.setMessage("There is no such URL!");
                errorResponse.setPath(request.getRequestURI());

                return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            RedirectResponse redirectResponse = new RedirectResponse(url.getLongUrl());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectResponse.getUrl());
            return new ResponseEntity<String>(headers,HttpStatus.FOUND);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            errorResponse.setMessage(e.getMessage());
            errorResponse.setPath(request.getRequestURI());

            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
