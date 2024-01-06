package com.fantasymap;

import com.fantasymap.handlers.helpers.JwtMiddleware;
import com.fantasymap.helpers.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
@JwtMiddleware()
public class TestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String result = "Test";
        byte[] resultBytes = result.getBytes(Charset.defaultCharset());

        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, resultBytes.length);

        try(OutputStream os = exchange.getResponseBody()){
            os.write(resultBytes);
        }
    }
}
