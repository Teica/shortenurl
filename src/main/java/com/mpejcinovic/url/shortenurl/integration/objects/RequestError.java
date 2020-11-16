package com.mpejcinovic.url.shortenurl.integration.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RequestError Infobip class
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestError {
    private ServiceException serviceException;
}
