package com.mpejcinovic.url.shortenurl.integration.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSRequest {

    private Tracking tracking;
    private SendingSpeedLimit sendingSpeedLimit;
    private List<Message> messages;
    private String bulkId;
}
