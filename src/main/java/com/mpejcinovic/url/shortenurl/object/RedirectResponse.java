package com.mpejcinovic.url.shortenurl.object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.http.ResponseEntity;

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
@ApiModel(description = "Response model that describes the outcome of the method for reversing a shortened form of a URL.", value = "Original URL")
public class RedirectResponse {

    @NonNull
    @ApiModelProperty(notes = "Shortened form of a URL", example = "http://stackoverflow.com/questions/1567929/website-safe-data-accessarchitecture-question?rq=1")
    private String url;

}