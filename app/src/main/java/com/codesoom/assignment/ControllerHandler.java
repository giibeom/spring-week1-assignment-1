package com.codesoom.assignment;

import com.codesoom.assignment.enums.ContextContainer;
import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.models.Path;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ControllerHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethodType method = HttpMethodType.getMethod(exchange.getRequestMethod());
        String uriPath = exchange.getRequestURI().getPath();
        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
        ContextContainer context = ContextContainer.getContext(uriPath);
        Path path = new Path(context.getContextValue(), uriPath.substring(context.getContextValue().length()));

        switch (context) {
            case TASKS:
                TaskControllerAdvice taskControllerAdvice = TaskControllerAdvice.getInstance();
                taskControllerAdvice.requestMapping(exchange, method, path, requestBody);
                break;
            default:
                ClientError.notFound(exchange);
        }
    }
}
