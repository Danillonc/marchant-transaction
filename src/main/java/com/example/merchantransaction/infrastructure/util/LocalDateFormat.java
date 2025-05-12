package com.example.merchantransaction.infrastructure.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFormat {
    private static String PATTERN = "dd/MM/yyyy";

    private LocalDateFormat() {}

    public static String convertLocalDateToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return localDateTime.format(formatter);
    }
}
