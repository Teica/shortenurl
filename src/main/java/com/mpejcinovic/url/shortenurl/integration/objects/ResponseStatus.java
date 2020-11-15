package com.mpejcinovic.url.shortenurl.integration.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatus {
    private int groupId;
    private String groupName;
    private int id;
    private String name;
    private String description;
}
