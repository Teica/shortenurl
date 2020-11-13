package com.mpejcinovic.url.shortenurl.object;

/**
 * The StatusCode enum contains all codes
 * the service can return in response
 *
 * @author Matea Pejčinović
 * @version 0.01.000
 * @since 13.11.2020.
 */
public enum StatusCode {
    NO_ERROR(200),
    GENERAL_ERROR(900);

    private final Integer statusCode;

    StatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Getter for status code
     *
     * @return Integer value for status code
     */
    public Integer getStatusCode() {
        return statusCode;
    }
}
