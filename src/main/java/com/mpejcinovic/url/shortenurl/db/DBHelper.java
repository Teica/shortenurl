package com.mpejcinovic.url.shortenurl.db;

import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.object.Url;
import lombok.NoArgsConstructor;

import java.sql.*;

@NoArgsConstructor
public class DBHelper {
    Connection connection = null;

    DBProperties props;

    public DBHelper(DBProperties dbProperties) {
        props = dbProperties;
    }

    private Connection getConnection() {
        try {
            connection = DriverManager.getConnection(props.getJdbcURL(), props.getJdbcUsername(), props.getJdbcPassword());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return connection;
    }

    public Url getUrlByLongUrl(String longUrl) {

        System.out.println("Long URL is " + longUrl);
        Url url = null;

        try {
            ResultSet rs;

            System.out.println("Prepared statement");
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM URL WHERE LONG_URL=?");
            statement.setString(1, longUrl);

            System.out.println("Executing...");
            rs = statement.executeQuery();

            while (rs.next()) {
                String shortUrl = rs.getString("short_url");
                System.out.println("Short URL: " + shortUrl);
                long id = rs.getLong("id");
                System.out.println("ID: " + id);
                url = new Url();
                url.setId(id);
                url.setLongUrl(longUrl);
                url.setShortUrl(shortUrl);
                System.out.println("Long URL: " + longUrl);
            }
            getConnection().close();
        } catch (Exception e) {
            System.err.println("Got an exception! " + e);
        }
        return url;
    }

    public Url getUrlById(long id) {

        System.out.println("ID is " + id);
        Url url = null;

        try {
            ResultSet rs;

            System.out.println("Prepared statement");
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM URL WHERE ID=?");
            statement.setLong(1, id);

            System.out.println("Executing...");
            rs = statement.executeQuery();

            while (rs.next()) {
                url = new Url();
                url.setId(id);
                url.setLongUrl(rs.getString("long_url"));
                url.setShortUrl(rs.getString("short_url"));
            }
            getConnection().close();
        } catch (Exception e) {
            System.err.println("Got an exception! " + e);
        }
        return url;
    }

    public int insertUrl(Url url) {

        int id = -1;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO URL (LONG_URL, SHORT_URL) VALUES (?,?)");

            preparedStatement.setString(1, url.getLongUrl());
            preparedStatement.setString(2, url.getShortUrl());

            id = preparedStatement.executeUpdate();
            System.out.println(id);

            getConnection().close();
        } catch (
                Exception e) {
            System.err.println("Got an exception! " + e);
        }
        return id;
    }
}
