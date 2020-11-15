package com.mpejcinovic.url.shortenurl.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpejcinovic.url.shortenurl.integration.objects.Destination;
import com.mpejcinovic.url.shortenurl.integration.objects.Message;
import com.mpejcinovic.url.shortenurl.integration.objects.SMSRequest;
import com.mpejcinovic.url.shortenurl.integration.objects.SMSResponse;
import com.mpejcinovic.url.shortenurl.object.ErrorResponse;
import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class InfobipSMSClient {

    String text;
    String endpoint;
    String key;

    public InfobipSMSClient(String text, String endpoint, String key) {
        this.text = text;
        this.endpoint = endpoint;
        this.key = key;
    }

    @SneakyThrows
    public String sendSMS() {

        System.out.println("Method sendSMS started!");

        URL _url = new URL(endpoint);
        HttpURLConnection urlConn = (HttpURLConnection) _url.openConnection();
        urlConn.setRequestMethod("POST");
        urlConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConn.setRequestProperty("Accept", "application/json");
        urlConn.setRequestProperty("Authorization", "App " + decodeAPIKey());

        urlConn.setDoOutput(true);
        urlConn.connect();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConn.getOutputStream()));
        writer.write(prepareInvoiceRequest());
        writer.flush();
        writer.close();

        InputStream is;

        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            is = urlConn.getInputStream();
        } else {
            is = urlConn.getErrorStream();
        }
        String response = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            response = sb.toString();
            System.out.println("Response: " + response);
        } catch (Exception e) {

        }

        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            SMSResponse smsResponse = new SMSResponse();
            Gson gson = new Gson();
            smsResponse = gson.fromJson(response, SMSResponse.class);
            return "Message ID: " + smsResponse.getMessages().get(0).getMessageId();
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            Gson gson = new Gson();
            errorResponse = gson.fromJson(response, ErrorResponse.class);
            return "Error message: " + errorResponse.getError();
        }
    }

    private String decodeAPIKey() {
        return new String(Base64.getDecoder().decode(key.getBytes()));
    }

    private String prepareInvoiceRequest() {

        SMSRequest smsRequest = new SMSRequest();

        Message message = new Message();
        message.setFrom("InfoSMS");

        message.setText(text);

        Destination destination = new Destination();
        destination.setTo("41793026727");

        message.setDestinations(new ArrayList<>());
        message.getDestinations().add(destination);

        message.setNotify(true);
        message.setValidityPeriod(40);

        smsRequest.setMessages(new ArrayList<>());
        smsRequest.getMessages().add(message);


        String json = new GsonBuilder().create().toJson(smsRequest, SMSRequest.class);
        System.out.println("JSON request: " + json);
        return json;
    }

}
