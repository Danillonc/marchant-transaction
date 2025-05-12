package com.example.merchantransaction.infrastructure.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LocalDateTimeFlexibleDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();

        // Case 1: Handle "String" format (e.g., "15/03/2020")
        if (token == JsonToken.VALUE_STRING) {
            String text = p.getText().trim();
            try {
                // Try to parse as a custom date format (e.g., "dd/MM/yyyy")
                return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                // Handle invalid format
                throw InvalidFormatException.from(p, "Invalid date string for LocalDateTime", text, LocalDateTime.class);
            }
        }

        // Case 2: Handle "Array" format (e.g., [2025, 5, 7, 15, 21, 32, 119040668])
        else if (token == JsonToken.START_ARRAY) {
            List<Integer> dateArray = p.readValueAs(List.class);
            if (dateArray.size() >= 3) {
                int year = dateArray.get(0);
                int month = dateArray.get(1);
                int day = dateArray.get(2);

                int hour = (dateArray.size() > 3) ? dateArray.get(3) : 0;
                int minute = (dateArray.size() > 4) ? dateArray.get(4) : 0;
                int second = (dateArray.size() > 5) ? dateArray.get(5) : 0;
                int nanoOfSecond = (dateArray.size() > 6) ? dateArray.get(6) : 0;

                return LocalDateTime.of(year, month, day, hour, minute, second, nanoOfSecond);
            } else {
                throw InvalidFormatException.from(p, "Array must contain at least year, month, and day", p.getText(), LocalDateTime.class);
            }
        }

        // If it's neither a valid string nor array, throw an exception
        else {
            throw InvalidFormatException.from(p, "Expected a string or array for LocalDateTime", p.getText(), LocalDateTime.class);
        }
    }
}