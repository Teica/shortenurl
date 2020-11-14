package com.mpejcinovic.url.shortenurl.helpers;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class UrlHelper {

    public static boolean isUrlValid(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    public static String shortenUrl(int id) {

        StringBuilder chars = new StringBuilder();

        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = LongStream.rangeClosed(0, 9)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        chars.append(lowercase).append(lowercase.toUpperCase()).append(numbers);

        char[] urlCharacters = chars.toString().toCharArray();

        StringBuffer shortenedUrl = new StringBuffer();

        System.out.println("ID for shortening: " + id);
        while (id > 0) {
            shortenedUrl.append(urlCharacters[id % 62]);
            id = id / 62;
        }

        System.out.println("shortenedUrl: " + shortenedUrl.reverse().toString());

        return shortenedUrl.reverse().toString();
    }

    public static String checkUrl(String url) {
        //provjeri ima li neki ovakav url unutra

        if (url.charAt(url.length() - 1) == '/')
            url = url.substring(0, url.length() - 1);

        return "";
    }

    public static int shortURLtoID(String shortURL)
    {
        int id = 0;
        for (int i = 0; i < shortURL.length(); i++)
        {
            if ('a' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'z')
                id = id * 62 + shortURL.charAt(i) - 'a';
            if ('A' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'Z')
                id = id * 62 + shortURL.charAt(i) - 'A' + 26;
            if ('0' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= '9')
                id = id * 62 + shortURL.charAt(i) - '0' + 52;
        }
        return id;
    }

}
