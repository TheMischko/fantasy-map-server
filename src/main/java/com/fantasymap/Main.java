package com.fantasymap;

import com.fantasymap.handlers.LoginHandler;
import com.fantasymap.handlers.UserHandler;
import com.fantasymap.handlers.helpers.MiddlewareProcessor;
import com.fantasymap.helpers.auth.AuthService;
import com.fantasymap.helpers.auth.JwtAuthService;
import com.fantasymap.managers.UserManager;
import com.fantasymap.models.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.logging.LogFilter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv("SERVER_PORT"));

        HttpServer server = CreateServer(port);
        if(server == null) return;

        Map<Class<?>, Object> dependencyContainer = createDependencyContainer();

        addHandler("/test", TestHandler.class, server, dependencyContainer);
        addHandler("/user", UserHandler.class, server, dependencyContainer);
        addHandler("/login", LoginHandler.class, server, dependencyContainer);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        server.setExecutor(threadPoolExecutor);

        server.start();
        System.out.println("Server started on port: " + port);
    }

    public static HttpServer CreateServer(int port){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            return server;
        } catch (IOException exception){
            System.err.println("Server couldn't be created. \n" + exception.getLocalizedMessage());
        }
        return null;
    }

    public static void addHandler(String path, Class<?> handlerClass, HttpServer server, Map<Class<?>, Object> dependencyContainer){
        try{
            HttpHandler handler = MiddlewareProcessor.processMiddleware(handlerClass, dependencyContainer);
            server.createContext(path, handler);
        } catch (Exception ex){
            System.out.println("Couldn't create a handler: " + handlerClass.getName());
            ex.printStackTrace();
        }
    }

    public static Map<Class<?>, Object> createDependencyContainer(){
        Map<Class<?>, Object> dependencies = new HashMap<>();

        JwtAuthService authService = new JwtAuthService();
        dependencies.put(UserManager.class, new UserManager());
        dependencies.put(AuthService.class, authService);
        dependencies.put(JwtAuthService.class, authService);

        return dependencies;
    }
}

