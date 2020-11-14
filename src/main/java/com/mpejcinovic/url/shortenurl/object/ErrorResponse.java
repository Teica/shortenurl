package com.mpejcinovic.url.shortenurl.object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@ApiModel(description = "Response model for errors that may occur during the execution of the methods.", value = "Error response")
public class ErrorResponse {
    @NonNull
    @ApiModelProperty(notes = "A timestamp of the response", example = "2020-11-14T13:33:16.839+0000")
    private String timestamp;

    @NonNull
    @ApiModelProperty(notes = "A status of the error", example = "400")
    private int status;

    @NonNull
    @ApiModelProperty(notes = "A description of the error", example = "Bad Request")
    private String error;

    @NonNull
    @ApiModelProperty(notes = "A message that further describes the error that occurred during the execution of the method", example = "Provided body was not submitted in required form!")
    private String message;

    @NonNull
    @ApiModelProperty(notes = "A path of the invoked method", example = "/shortenurl/shorten")
    private String path;
}
