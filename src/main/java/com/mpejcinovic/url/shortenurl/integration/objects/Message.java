package com.mpejcinovic.url.shortenurl.integration.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Message Infobip class
 *
 * @author Matea Pejcinovic
 * @version 0.00.004
 * @since 15.11.2020.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String text;
    private String notifyUrl;
    private String callbackData;
    private Language language;
    private int validityPeriod;
    private boolean flash;
    private List<Destination> destinations;
    private List<String> to;
    private String sendAt;
    private String notifyContentType;
    private String from;
    private String transliteration;
    private String intermediateReport;
    private DeliveryTimeWindow deliveryTimeWindow;
    private Binary binary;
    private boolean notify;
    private Regional regional;
}
