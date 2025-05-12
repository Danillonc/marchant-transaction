package com.example.merchantransaction.infrastructure.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFormat {
    private static String PATTERN = "dd/MM/yyyy";

    private LocalDateFormat() {}

    public static String convertLocalDateToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return localDateTime.format(formatter);
    }

    public static LocalDate convertStringToLocalDate(String stringDate) {
        return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(PATTERN));
    }
}
