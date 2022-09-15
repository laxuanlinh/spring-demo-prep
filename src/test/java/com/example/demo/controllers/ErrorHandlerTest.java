package com.example.demo.controllers;

import com.example.demo.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.SQLDataException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ErrorHandlerTest {

    private final ErrorHandler handler = new ErrorHandler();

    @Test
    public void testHandleGenericExceptions(){
        var e = new SQLDataException("Unable to connect to database");
        var response = handler.handleGenericExceptions(e);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something wrong happened in the server", response.getBody().get("message"));
    }

    @Test
    public void testHandleDataException(){
        var e = new SQLDataException("Unable to connect to database");
        var response = handler.handleDataException(e);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Data could not be saved or retrieved", response.getBody().get("message"));
    }

    @Test
    public void testInvalidInputException(){
        var e = new InvalidInputException("Title cannot be null or empty");
        var response = handler.handleInvalidInputException(e);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Title cannot be null or empty", response.getBody().get("message"));
    }

}