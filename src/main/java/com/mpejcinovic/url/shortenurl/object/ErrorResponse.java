package com.mpejcinovic.url.shortenurl.object;

import lombok.*;

/**
 * Response for errors
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class ErrorResponse {
    @NonNull
    private String timestamp;

    @NonNull
    private int status;

    @NonNull
    private String error;

    @NonNull
    private String message;

    @NonNull
    private String path;
}
