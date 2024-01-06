package com.fantasymap.handlers;

import com.fantasymap.helpers.PasswordHasher;
import com.fantasymap.helpers.auth.AuthService;
import com.fantasymap.managers.UserManager;
import com.fantasymap.modelClasses.UserDB;
import com.fantasymap.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    private UserManager userManager;
    private AuthService authService;
    public LoginHandler(UserManager userManager, AuthService authService){
        this.userManager = userManager;
        this.authService = authService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputStream);
            String name = node.get("name").asText();
            String password = node.get("password").asText();

            UserDB user = this.userManager.findUser(name);
            if(user == null){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.close();
                return;
            }
            boolean passwordMatches = PasswordHasher.arePasswordsEqual(password,user.password);
            if(!passwordMatches){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.close();
            }

            String token = this.authService.authenticate(user.name, user.user_id);
            byte[] tokenBytes = token.getBytes();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, tokenBytes.length);
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(tokenBytes);
            outputStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
