package com.mpejcinovic.url.shortenurl.controllers;

import com.mpejcinovic.url.shortenurl.configuration.Config;
import com.mpejcinovic.url.shortenurl.object.Status;
import com.mpejcinovic.url.shortenurl.object.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The StatusController class handles the methods
 * for checking a status of the service. It should
 * be used in order to get information is service
 * running currently or not.
 *
 * @author Matea Pejcinovic
 * @version 0.00.002
 * @since 13.11.2020.
 */
@RestController
public class StatusController {
    @Autowired
    private Config config;
    private static final Logger LOGGER = LogManager.getLogger(StatusController.class);

    /**
     * Get the information is the service available
     *
     * @return Status object - 200 OK is expected
     * to be received if the service is up and running
     * @see Status
     */
    @GetMapping(value = "/status")
    public Status status() {
        LOGGER.info("Method started!");
        return Status.of(
                StatusCode.NO_ERROR.getStatusCode(),
                StringUtils.capitalize(StatusCode.NO_ERROR.name().replace('_', ' ').toLowerCase()));
    }
}
