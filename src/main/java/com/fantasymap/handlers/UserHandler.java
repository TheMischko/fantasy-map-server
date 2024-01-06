package com.fantasymap.handlers;

import com.fantasymap.handlers.helpers.JwtMiddleware;
import com.fantasymap.helpers.Logger;
import com.fantasymap.helpers.PasswordHasher;
import com.fantasymap.managers.UserManager;
import com.fantasymap.models.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.javalite.activejdbc.annotations.IdName;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
public class UserHandler implements HttpHandler {
    UserManager userManager;
    public UserHandler(UserManager userManager){
        this.userManager = userManager;
        System.out.println("Listening for /user calls.");
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        System.out.println("Got /user call " + requestMethod);

        if("POST".equals(requestMethod)){
            createUser(exchange);
        } else {
            getUserNames(exchange);
        }
    }

    public void createUser (HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputStream);
            String name = node.get("name").asText();
            String password = node.get("password").asText();
            this.userManager.addUser(name, password);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            byte[] resultBytes = "success".getBytes(Charset.defaultCharset());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resultBytes);
            }
        } catch (JsonProcessingException e){
            System.err.println("Problem with creating a user.");
            e.printStackTrace();
        }
    }

    public void getUserNames(HttpExchange exchange){
        try {
            List<String> userNames = userManager.getUserNames();
            System.out.println("User names " + userNames.toString());
            ObjectMapper mapper = new ObjectMapper();
            String userNamesJSON = mapper.writeValueAsString(userNames);
            System.out.println("User names json" + userNamesJSON);
            byte[] resultBytes = userNamesJSON.getBytes(Charset.defaultCharset());

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, resultBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resultBytes);
            }
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
