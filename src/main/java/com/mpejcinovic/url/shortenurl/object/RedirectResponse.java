package com.mpejcinovic.url.shortenurl.object;

import lombok.*;

/**
 * RedirectResponse is a class that presents
 * the response for reversing a shortened URL.
 * It contains single property that returns the
 * original form of the url for which a shorten
 * version is submitted in a request.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 14.11.2020.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class RedirectResponse {

    @NonNull
    private String url;

}