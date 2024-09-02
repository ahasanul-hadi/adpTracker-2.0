package com.tracker.adp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ThymeleafException extends RuntimeException{
    private final String message;
    private final String reason;
    private final HttpStatus httpStatus;

}
