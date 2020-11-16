package com.mpejcinovic.url.shortenurl.object;

import lombok.*;

/**
 * The StatusWS class contains two properties: code for
 * storing the operation's status code and message
 * for storing a description of the outcome of the method.
 *
 * @author Matea Pejčinović
 * @version 0.01.000
 * @since 13.11.2020.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class Status {

    @NonNull
    private int code;

    @NonNull
    private String message;
}
