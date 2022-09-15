package com.example.demo.controllers;

import com.example.demo.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler {


    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGenericExceptions(Exception e){
        log.info("Handling exception "+e.getMessage());
        log.error(e.getMessage(), e);
        Map<String, String> message = new HashMap<>();
        message.put("message", "Something wrong happened in the server");
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = SQLDataException.class)
    public ResponseEntity<Map<String, String>> handleDataException(SQLDataException e){
        log.info("Handling exception "+e.getMessage());
        log.error(e.getMessage(), e);
        Map<String, String> message = new HashMap<>();
        message.put("message", "Data could not be saved or retrieved");
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<Map<String, String>> handleInvalidInputException(InvalidInputException e){
        log.info("Handling exception "+e.getMessage());
        log.error(e.getMessage(), e);
        Map<String, String> message = new HashMap<>();
        message.put("message", e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.info("Handling exception "+e.getMessage());
        log.error(e.getMessage(), e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
