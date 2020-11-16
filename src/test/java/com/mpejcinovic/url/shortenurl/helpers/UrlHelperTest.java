package com.mpejcinovic.url.shortenurl.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UrlHelperTest {

    @Test
    void isUrlValid_URLValid_ReturnTrue() {
        System.out.println("-> isUrlValid_URLValid_ReturnTrue executing...");

        String validUrl = "https://stackoverflow.com/";
        Assertions.assertTrue(UrlHelper.isUrlValid(validUrl));
    }

    @Test
    void isUrlValid_URLInvalid_ReturnFalse() {
        System.out.println("-> isUrlValid_URLInvalid_ReturnFalse executing...");

        String validUrl = "https://stackoverflow.comm/";
        Assertions.assertFalse(UrlHelper.isUrlValid(validUrl));
    }

    @Test
    void shortenUrl_IDProvided_ShorteningSuccessful() {
        System.out.println("-> shortenUrl_IDProvided_ShorteningSuccessful executing...");

        int id = 1;
        String hash = "b";

        Assertions.assertTrue(UrlHelper.shortenUrl(id).equals(hash));
    }

    @Test
    void shortURLtoID_HashProvided_ReversingSuccessful() {
        System.out.println("-> shortURLtoID_HashProvided_ReversingSuccessful executing...");

        int id = 1;
        String hash = "b";

        Assertions.assertTrue(UrlHelper.shortURLtoID(hash) == 1);
    }

    @Test
    void prepareUrl_UrlProvided_UrlModified() {
        System.out.println("-> prepareUrl_UrlProvided_UrlModified executing...");

        String url = "https://stackoverflow.com/";
        String modifiedUrl = "://stackoverflow.com/";

        Assertions.assertTrue(UrlHelper.prepareUrl(url).equals(modifiedUrl));
    }

    @Test
    void isInfobipUrl_UrlInfobip_ReturnTrue() {
        System.out.println("-> isInfobipUrl_UrlInfobip_ReturnTrue executing...");

        String url = "https://www.infobip.com/";

        Assertions.assertTrue(UrlHelper.isInfobipUrl(url));
    }

    @Test
    void isInfobipUrl_UrlNotInfobip_ReturnFalse() {
        System.out.println("-> isInfobipUrl_UrlNotInfobip_ReturnFalse executing...");

        String url = "https://www.infobipp.com/";

        Assertions.assertFalse(UrlHelper.isInfobipUrl(url));
    }

}
