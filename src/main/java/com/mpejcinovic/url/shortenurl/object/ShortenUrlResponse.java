package com.mpejcinovic.url.shortenurl.object;

import lombok.*;

/**
 * ShortenUrlResponse is a class that presents
 * the response for shortening url. It contains
 * single property that returns the shortened
 * form of the url provided in the request for
 * shortening a URL.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 13.11.2020.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class ShortenUrlResponse {

    @NonNull
    private String shortUrl;

}
