package com.mpejcinovic.url.shortenurl.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Url {
    @Id
    @GeneratedValue
    private Long id;
    private String longUrl;
    private String shortUrl;
    private LocalDate submitDate;
}
