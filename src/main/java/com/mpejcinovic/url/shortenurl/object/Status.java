package com.mpejcinovic.url.shortenurl.object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Specifies the outcome of the operation using the code and the message that closely describe the cause for a specific code.", value = "Status")
public class Status {

    @ApiModelProperty(notes = "The code of the status", example = "200")
    @NonNull
    private int code;

    @ApiModelProperty(notes = "The description of the code of the status", example = "No error!")
    @NonNull
    private String message;
}
