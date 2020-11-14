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
import java.time.LocalDateTime;
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

        //pripremi URL za https i http
        String preparedUrl = shortenUrlRequest.getUrl().trim();

        DBHelper dbHelper = new DBHelper(dbProperties);

        if (!UrlHelper.isUrlValid(preparedUrl)) {

            System.out.println(preparedUrl + " is not valid!");

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(LocalDateTime.now().toString());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setError(HttpStatus.BAD_REQUEST.name());
            errorResponse.setMessage("");
            errorResponse.setPath(request.getRequestURI());

            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Url existingUrl = dbHelper.getUrlByLongUrl(preparedUrl);

        if (existingUrl != null){
            System.out.println("URL already inserted!");
            shortenUrlResponse = prepareShortenedUrlResponse(request, existingUrl.getShortUrl());
            return new ResponseEntity(shortenUrlResponse, HttpStatus.OK);
        }

        String shortenUrl = UrlHelper.shortenUrl(counter.get());
        if (shortenUrl != null && shortenUrl.length() > 0) {

            Url url = new Url();
            url.setShortUrl(shortenUrl);
            url.setLongUrl(preparedUrl);

            int id = dbHelper.insertUrl(url);
            counter.getAndIncrement();

            shortenUrlResponse = prepareShortenedUrlResponse(request, shortenUrl);
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

    private ShortenUrlResponse prepareShortenedUrlResponse(HttpServletRequest request, String shortenUrl) {
        ShortenUrlResponse shortenUrlResponse;
        String requestUrl = request.getRequestURI();
        String context = requestUrl.substring(0, requestUrl.lastIndexOf("shorten") - 1);
        System.out.println("Context: " + context);

        StringBuilder finalShortenedUrl = new StringBuilder();
        finalShortenedUrl.append("http://").append(request.getHeader("Host")).append(context).append("/").append(shortenUrl);

        shortenUrlResponse = new ShortenUrlResponse(finalShortenedUrl.toString());
        System.out.println("Shorten URL: " + shortenUrl);
        return shortenUrlResponse;
    }

    @ResponseBody
    @GetMapping(value = "{hash}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOriginalLinkAndRedirect(HttpServletResponse httpServletResponse, @PathVariable String hash) {
        System.out.println("Method getOriginalLinkAndRedirect started!");
        System.out.println("Hash: " + hash);
        DBHelper dbHelper = new DBHelper(dbProperties);

        int id = UrlHelper.shortURLtoID(hash);
        System.out.println("ID je: " + id);

        Url url = dbHelper.getUrlById(id);

        RedirectResponse redirectResponse = new RedirectResponse(url.getLongUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", url.getLongUrl());

        try {
            httpServletResponse.sendRedirect(url.getLongUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(redirectResponse,HttpStatus.FOUND);
    }

}