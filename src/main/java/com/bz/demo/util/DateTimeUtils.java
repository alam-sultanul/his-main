package com.bz.demo.util;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateTimeUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    public static Date toDate(@Nonnull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(@Nonnull Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public static String getCurrentDate(){
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = new Date();
        String str = "";

        try {
            str = outputFormat.format(date);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static LocalDate convertStringToLocaleDate(String date) {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date,formatter);
        return localDate;
    }

    public static Date convertStringToDate(String date) {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date,formatter);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String convertDateToString(LocalDate date) {
        String outPattern = "dd-MM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outPattern);
        String formattedDate = date.format(formatter);

        return formattedDate;
    }
}
