package com.mpejcinovic.url.shortenurl.object;

import lombok.*;

/**
 * ShortenUrlRequest is a class that contains
 * a URL that should be shortened.
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
public class ShortenUrlRequest {

    @NonNull
    private String url;

}
