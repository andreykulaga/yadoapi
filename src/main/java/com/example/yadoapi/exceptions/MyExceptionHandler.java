package com.example.yadoapi.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class MyExceptionHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) throws JsonProcessingException {
        String bodyOfResponse = objectMapper.writeValueAsString(new MyExceptionResponse(400, "Validation failed"));
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) throws JsonProcessingException {
        String bodyOfResponse = objectMapper.writeValueAsString(new MyExceptionResponse(404, "Item not found"));
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

}
