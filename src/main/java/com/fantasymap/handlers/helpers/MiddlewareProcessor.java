package com.fantasymap.handlers.helpers;


import com.fantasymap.helpers.auth.JwtAuthService;
import com.sun.net.httpserver.HttpHandler;
import java.util.Map;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MiddlewareProcessor {
    public static HttpHandler processMiddleware(Class<?> handlerClassType, Map<Class<?>, Object> dependencies) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = findSuitableConstructor(handlerClassType);
        Object[] params = resolveConstructorParams(constructor, dependencies);
        HttpHandler originalHandler = (HttpHandler) constructor.newInstance(params);
        originalHandler = appendBasicHandlers(originalHandler);

        if(handlerClassType.isAnnotationPresent(JwtMiddleware.class)){
            originalHandler = new JwtMiddlewareHandler(originalHandler, (JwtAuthService) dependencies.get(JwtAuthService.class));
        }

        return originalHandler;
    }

    private static Constructor<?> findSuitableConstructor(Class<?> handlerClassType) throws NoSuchMethodException {
        Constructor<?>[] constructors = handlerClassType.getConstructors();
        if (constructors.length == 0) {
            throw new NoSuchMethodException("No public constructors found for class: " + handlerClassType.getName());
        }
        return constructors[0];
    }

    private static Object[] resolveConstructorParams(Constructor<?> constructor, Map<Class<?>, Object> dependencies) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            if(dependencies.containsKey(paramType)){
                params[i] = dependencies.get(paramType);
            } else {
                throw new IllegalArgumentException("Dependency not found for type: " + paramType);
            }
        }
        return params;
    }

    private static HttpHandler appendBasicHandlers(HttpHandler originalHandler){
        HttpHandler handler = originalHandler;
        handler = new CorsMiddleware(handler);
        handler = new LoggingMiddleware(handler);
        return handler;
    }
}
