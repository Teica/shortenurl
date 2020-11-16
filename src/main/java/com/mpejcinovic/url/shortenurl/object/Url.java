package com.mpejcinovic.url.shortenurl.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * A class for saving original and shortened URL.
 * It also contains a date of submission and an
 * identifier.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 14.11.2020.
 */
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
