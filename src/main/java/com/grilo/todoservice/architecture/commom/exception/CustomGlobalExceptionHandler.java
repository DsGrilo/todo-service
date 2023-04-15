package com.grilo.todoservice.architecture.commom.exception;


import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public final ResponseEntity<Object> handleAllExceptions(GenericException ex, HttpServletRequest httpServletRequest) {
        final Map<String, Object> body = new LinkedHashMap<>();
        try {
            body.put("message", ex.getMessage());
        } catch (NoSuchMessageException | IllegalArgumentException e) {
            body.put("message", "ERROR");
            e.printStackTrace();
        }
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<Object> springHandleIncomplete(NullPointerException ex) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("message", ex.getMessage());

        return new ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(MethodNotAllowedException.class)
    public final ResponseEntity<Object> methodNotAllowed(HttpClientErrorException.MethodNotAllowed ex){
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("message", ex.getMessage());

        return new ResponseEntity(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    public final ResponseEntity<Object> tokenExpired(RuntimeException ex, WebRequest request){
        System.out.println("TESTE");
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("message", ex.getMessage());
        return new ResponseEntity(body, HttpStatus.UNAUTHORIZED);
    }



}

