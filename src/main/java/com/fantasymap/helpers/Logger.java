package com.fantasymap.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Logger {
    public static void logRequestDetails(HttpExchange exchange){
        String timestamp = getTimestamp();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        int statusCode = exchange.getResponseCode();

        System.out.printf("[%s] %s %s %s%n", timestamp, method, statusCode, path);
    }

    private static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}
