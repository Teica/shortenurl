package com.mpejcinovic.url.shortenurl.object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Response model that describes the outcome of the method for shortening a URL.", value = "Shortened URL response")
public class ShortenUrlResponse {

    @NonNull
    @ApiModelProperty(notes = "Shortened form of a URL", example = "http://service.com/xYzw3b")
    private String shortUrl;

}
