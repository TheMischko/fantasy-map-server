package com.fantasymap.handlers.helpers;

import com.fantasymap.helpers.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LoggingMiddleware extends BaseMiddleware{
    public LoggingMiddleware(HttpHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        Logger.logRequestDetails(exchange);
    }
}
