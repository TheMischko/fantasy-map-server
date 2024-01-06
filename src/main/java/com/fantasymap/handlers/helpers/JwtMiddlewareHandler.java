package com.fantasymap.handlers.helpers;

import com.fantasymap.helpers.auth.JwtAuthService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

public class JwtMiddlewareHandler extends BaseMiddleware {
    JwtAuthService jwtAuthService;
    public JwtMiddlewareHandler(HttpHandler nextHandler, JwtAuthService jwtAuthService) {
        super(nextHandler);
        this.jwtAuthService = jwtAuthService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(this.isValidJwtToken(exchange)){
            super.handle(exchange);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
            OutputStream os = exchange.getResponseBody();
            os.close();
        }

    }

    private boolean isValidJwtToken(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().get("Authorization").get(0);
        String[] authParts = auth.split(" ");
        System.out.println(authParts[1]);
        return jwtAuthService.isValid(authParts[1]);
    }
}
