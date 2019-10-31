package com.horovod.android.chicksandfreakscounter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static final DateFormat dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

    public static String formatDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        return dateTimeFormatter.format(date);
    }

    public static String clearGaps(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String result = input.trim().replaceAll("\\s{2,}", " ");
        return result;
    }

}
