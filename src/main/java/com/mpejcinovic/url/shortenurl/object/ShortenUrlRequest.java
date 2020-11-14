package com.mpejcinovic.url.shortenurl.object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "A model that describes a request for a method for shortening a URL", value = "A request for shortening a URL")
public class ShortenUrlRequest {

    @NonNull
    @ApiModelProperty(notes = "A URL for shortening", example = "http://stackoverflow.com/questions/1567929/website-safe-data- accessarchitecture-question?rq=1")
    private String url;

}
