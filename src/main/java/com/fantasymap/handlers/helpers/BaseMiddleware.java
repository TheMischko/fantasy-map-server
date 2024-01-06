package com.fantasymap.handlers.helpers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class BaseMiddleware implements HttpHandler {
    private final HttpHandler nextHandler;

    public BaseMiddleware(HttpHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        nextHandler.handle(exchange);
    }
}
