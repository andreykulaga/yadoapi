package com.example.yadoapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MyExceptionResponse {
    int code;
    String message;
}
