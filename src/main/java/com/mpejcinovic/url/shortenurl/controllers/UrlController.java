package com.mpejcinovic.url.shortenurl.controllers;

import com.mpejcinovic.url.shortenurl.configuration.Config;
import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.db.DBHelper;
import com.mpejcinovic.url.shortenurl.helpers.UrlHelper;
import com.mpejcinovic.url.shortenurl.object.*;
import io.swagger.annotations.*;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The URLController class handles the methods
 * for shortening URLs and reversing the shortened
 * URL to the initial one.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 13.11.2020.
 */
@Api(value = "URL Management", tags = "URL controller")
@RestController
public class UrlController {

    @Autowired
    private Config config;

    @Autowired
    private DBProperties dbProperties;

    private final AtomicInteger counter = new AtomicInteger(1);

    private static final Logger URL_LOGGER = LogManager.getLogger(UrlController.class);

    @ApiOperation(value = "Shortens a URL provided in a request.", response = ShortenUrlResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "No error", response = ShortenUrlResponse.class)
    })
    @ResponseStatus
    @PostMapping(value = "/shorten", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity shortenUrl(HttpServletRequest request,
                                     @ApiParam(value = "The request body with a URL that should be shortened", required = true) @Valid @RequestBody ShortenUrlRequest shortenUrlRequest) {
        URL_LOGGER.info("Method shortenUrl started.");
        ShortenUrlResponse shortenUrlResponse = null;

        String trimmedUrl = shortenUrlRequest.getUrl().trim();
        DBHelper dbHelper = new DBHelper(dbProperties);

        if (!UrlHelper.isUrlValid(trimmedUrl)) {

            System.out.println(trimmedUrl + " is not valid!");

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setError(HttpStatus.BAD_REQUEST.name());
            errorResponse.setMessage("");
            errorResponse.setPath(request.getRequestURI());

            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }

        int urlLength = trimmedUrl.length();
        if (trimmedUrl.substring(urlLength - 1, urlLength).equals("/")) {
            trimmedUrl = trimmedUrl.substring(0, urlLength - 1);
        }

        String preparedUrl = UrlHelper.prepareUrl(trimmedUrl);

        Url existingUrl = dbHelper.getUrlByLongUrl(preparedUrl);

        if (existingUrl != null){
            System.out.println("URL already inserted!");
            return new ResponseEntity(
                    new ShortenUrlResponse(existingUrl.getShortUrl()),
                    HttpStatus.OK);
        }

        Url lastUrl = dbHelper.getLastUrl();

        if (counter.get() == 1 && lastUrl != null){
            int idx = lastUrl.getShortUrl().lastIndexOf('/');
            System.out.println("short URL: " + lastUrl.getShortUrl());
            System.out.println("IDX: " + idx);

            String hash = lastUrl.getShortUrl().substring(idx + 1, lastUrl.getShortUrl().length());
            System.out.println("Hash: " + hash);
            int id = UrlHelper.shortURLtoID(hash);
            System.out.println("ID: " + id);
            counter.set(id + 1);
        }
        String shortenUrl = UrlHelper.shortenUrl(counter.get());
        if (shortenUrl != null && shortenUrl.length() > 0) {
            shortenUrl = prepareShortenedUrlResponse(request, shortenUrl);

            Url url = new Url();
            url.setShortUrl(shortenUrl);
            url.setLongUrl(trimmedUrl);
            url.setSubmitDate(LocalDate.now());

            int id = dbHelper.insertUrl(url);
            counter.getAndIncrement();

            shortenUrlResponse = new ShortenUrlResponse(shortenUrl);
            return new ResponseEntity(shortenUrlResponse, HttpStatus.OK);
        } else {
            System.out.println("Issue with Shorten URL: " + shortenUrl);

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
            errorResponse.setMessage("");
            errorResponse.setPath(request.getRequestURI());

            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private String prepareShortenedUrlResponse(HttpServletRequest request, String shortenUrl) {
        ShortenUrlResponse shortenUrlResponse;
        String requestUrl = request.getRequestURI();
        String context = requestUrl.substring(0, requestUrl.lastIndexOf("shorten") - 1);

        StringBuilder finalShortenedUrl = new StringBuilder();
        finalShortenedUrl.append("http://").append(request.getHeader("Host")).append(context).append("/").append(shortenUrl);

        System.out.println("Shorten URL: " + shortenUrl);
        return finalShortenedUrl.toString();
    }

    @ResponseBody
    @GetMapping(value = "{hash}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOriginalLinkAndRedirect(HttpServletResponse httpServletResponse, @PathVariable String hash) {
        System.out.println("Method getOriginalLinkAndRedirect started!");
        System.out.println("Hash: " + hash);
        DBHelper dbHelper = new DBHelper(dbProperties);

        int id = UrlHelper.shortURLtoID(hash);
        System.out.println("ID is: " + id);

        Url url = dbHelper.getUrlById(id);

        RedirectResponse redirectResponse = new RedirectResponse(url.getLongUrl());

        try {
            httpServletResponse.sendRedirect(redirectResponse.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(redirectResponse, HttpStatus.FOUND);
    }

}
