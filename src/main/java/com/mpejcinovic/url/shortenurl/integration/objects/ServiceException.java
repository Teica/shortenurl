package com.mpejcinovic.url.shortenurl.integration.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceException {
    private String text;
    private String variables;
    private String additionalDescription;
    private String messageId;
}
