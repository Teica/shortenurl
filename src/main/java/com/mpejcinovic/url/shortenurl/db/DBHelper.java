package com.mpejcinovic.url.shortenurl.db;

import com.mpejcinovic.url.shortenurl.configuration.DBProperties;
import com.mpejcinovic.url.shortenurl.object.Url;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

/**
 * Helper for interaction with a database.
 *
 * @author Matea Pejcinovic
 * @version 0.00.003
 * @since 14.11.2020.
 */
@NoArgsConstructor
public class DBHelper {
    Connection connection = null;

    DBProperties props;

    private static final Logger DB_HELPER_LOGGER = LogManager.getLogger(DBHelper.class);
    
    public DBHelper(DBProperties dbProperties) {
        props = dbProperties;
    }

    /**
     * Gets the connection to a database.
     *
     * @return a Connection object
     * @see Connection
     */
    private Connection getConnection() {
        try {
            connection = DriverManager.getConnection(props.getJdbcURL(), props.getJdbcUsername(), props.getJdbcPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * Retrieves last inserted URL.
     *
     * @return a URL representing last inserted object
     * @see Url
     */
    public Url getLastUrl() {
        DB_HELPER_LOGGER.info("Method getLastUrl started");
        Url url = null;

        try {
            ResultSet rs;
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("select * from url ORDER BY ID ASC");
            while (resultSet.next()) {
                if (resultSet.isLast()) {
                    url = new Url();
                    url.setId(resultSet.getLong("id"));
                    url.setLongUrl(resultSet.getString("long_url"));
                    url.setShortUrl(resultSet.getString("short_url"));
                    url.setSubmitDate(resultSet.getDate("submit_date").toLocalDate());
                }
            }

            getConnection().close();
        } catch (Exception e) {
            DB_HELPER_LOGGER.error("Got an exception! " + e);
        }
        return url;
    }

    /**
     * Gets the Url object based on original URL.
     *
     * @param longUrl original URL
     * @return a Url object
     * @see Url
     */
    public Url getUrlByLongUrl(String longUrl) {

        DB_HELPER_LOGGER.info("Long URL is " + longUrl);
        Url url = null;

        try {
            ResultSet rs;

            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM URL WHERE LONG_URL like ?");
            statement.setString(1, "%" + longUrl);
            rs = statement.executeQuery();

            while (rs.next()) {
                String shortUrl = rs.getString("short_url");
                long id = rs.getLong("id");
                url = new Url();
                url.setId(id);
                url.setLongUrl(longUrl);
                url.setShortUrl(shortUrl);
                DB_HELPER_LOGGER.debug("Long URL: " + longUrl);
            }
            getConnection().close();
        } catch (Exception e) {
            DB_HELPER_LOGGER.error("Got an exception! " + e);
        }
        return url;
    }

    /**
     * Gets Url object by identifier.
     *
     * @param id an identifier of a row in a database
     * @return a Url object
     * @see Url
     */
    public Url getUrlById(long id) {

        DB_HELPER_LOGGER.debug("ID is " + id);
        Url url = null;

        try {
            ResultSet rs;

            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM URL WHERE ID=?");
            statement.setLong(1, id);

            rs = statement.executeQuery();

            while (rs.next()) {
                url = new Url();
                url.setId(id);
                url.setLongUrl(rs.getString("long_url"));
                url.setShortUrl(rs.getString("short_url"));
            }
            getConnection().close();
        } catch (Exception e) {
            DB_HELPER_LOGGER.error("Got an exception! " + e);
        }
        return url;
    }

    /**
     * Inserts a Url object in a database.
     *
     * @param url a Url object to be inserted
     * @return an identifier of the inserted row
     * @see Url
     */
    public int insertUrl(Url url) {

        int id = -1;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO URL (LONG_URL, SHORT_URL, SUBMIT_DATE) VALUES (?, ?, ?)");

            preparedStatement.setString(1, url.getLongUrl());
            preparedStatement.setString(2, url.getShortUrl());
            preparedStatement.setDate(3, java.sql.Date.valueOf(url.getSubmitDate()));

            id = preparedStatement.executeUpdate();
            DB_HELPER_LOGGER.debug(id);

            getConnection().close();
        } catch (
                Exception e) {
            DB_HELPER_LOGGER.error("Got an exception! " + e);
        }
        return id;
    }

    /**
     * Gets the number of requests for yesterday.
     *
     * @param yesterday previous day
     * @return a number of requests for yesterday
     */
    public int getNumberOfRequestForPreviousDay(LocalDate yesterday) {

        DB_HELPER_LOGGER.debug("Yesterday is " + yesterday);
        int numberOfRequests = -1;

        try {
            ResultSet rs;

            PreparedStatement statement = getConnection().prepareStatement("SELECT COUNT(*) FROM URL WHERE SUBMIT_DATE=?");
            statement.setDate(1, java.sql.Date.valueOf(yesterday));

            rs = statement.executeQuery();

            if (rs.next()) {
                numberOfRequests = rs.getInt(1);
                DB_HELPER_LOGGER.debug("Number of requests: " + numberOfRequests);
            } else {
                DB_HELPER_LOGGER.debug("Error: could not get the record counts");
            }
            getConnection().close();
        } catch (Exception e) {
            DB_HELPER_LOGGER.error("Got an exception! " + e);
        }
        return numberOfRequests;

    }

    /**
     * Gets a number of requests in a certain range.
     *
     * @param startDate the start date for a date range
     * @param endDate the end date for a date range
     * @return a number of requests in a defined date range
     */
    public int getNumberOfRequestForDateRange(LocalDate startDate, LocalDate endDate) {
        DB_HELPER_LOGGER.debug("Start date: " + startDate + ", end date: " + endDate);
        int numberOfRequests = -1;

        try {
            ResultSet rs;

            PreparedStatement statement = getConnection().prepareStatement("SELECT COUNT(*) FROM URL WHERE SUBMIT_DATE BETWEEN ? AND ?");
            statement.setDate(1, java.sql.Date.valueOf(startDate));
            statement.setDate(2, java.sql.Date.valueOf(endDate));

            rs = statement.executeQuery();

            if (rs.next()) {
                numberOfRequests = rs.getInt(1);
                DB_HELPER_LOGGER.debug("Number of requests: " + numberOfRequests);
            } else {
                DB_HELPER_LOGGER.debug("Error: could not get the record counts");
            }
            getConnection().close();
        } catch (Exception e) {
            DB_HELPER_LOGGER.error("Got an exception! " + e);
        }
        return numberOfRequests;
    }
}
