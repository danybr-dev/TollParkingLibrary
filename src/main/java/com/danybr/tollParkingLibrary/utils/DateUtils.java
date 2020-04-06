package com.danybr.tollParkingLibrary.utils;

import com.danybr.tollParkingLibrary.exception.WrongDateException;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class DateUtils {

    private final static Logger LOGGER = Logger.getLogger(DateUtils.class.getName());
    private final static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";


    /**
     * Generate a date from a string in input.
     * A valid date is formatted as yyyy-MM-dd HH:mm:ss
     * @param stringDateTime a date in the format yyyy-MM-dd HH:mm:ss
     * @return the date as LocalDateTime
     * @throws WrongDateException the date must be not null and of the format yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime getDateTime(String stringDateTime) throws WrongDateException
    {
        if (stringDateTime == null)
            throw new WrongDateException("Provided date in null");
        if (stringDateTime.trim().equals(""))
            throw new WrongDateException("Provided date in empty");


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        try
        {
            return LocalDateTime.parse(stringDateTime, dateTimeFormatter);
        }
        // Date format is invalid
        catch (DateTimeParseException e)
        {
            throw new WrongDateException(stringDateTime + " is invalid date format");
        }
    }


    /**
     * Getting in input a startTime and endTime returns the number of hours to be paid by the customer.
     * A delay triggers an additional hour.
     * @param startTime start time
     * @param endTime end time
     * @return number of hour to pay
     * @throws WrongDateException end time can not be before start time
     */
    public static long getHoursDifferenceToPay(LocalDateTime startTime, LocalDateTime endTime) throws WrongDateException
    {
        if (!endTime.isAfter(startTime))
            throw new WrongDateException("End time can not be before start time");

        Duration duration = Duration.between(startTime, endTime);

        // Get hours between from and to
        long diffHours = duration.toHours();

        // Get minutes between from and to
        long diffMinutes = duration.toMinutes();

        // A delay triggers an additional hour.
        if (diffMinutes > 0)
            diffHours ++;

        return diffHours;
    }
}
