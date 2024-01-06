package com.fantasymap.handlers;

import com.fantasymap.helpers.JsonReader;
import com.fantasymap.models.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

public class MapHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Map> maps = Map.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "[]";

        byte[] resultBytes = result.getBytes(Charset.defaultCharset());

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resultBytes.length);

        try(OutputStream os = exchange.getResponseBody()){
            os.write(resultBytes);
        }
    }
}
