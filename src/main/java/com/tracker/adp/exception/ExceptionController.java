package com.tracker.adp.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size:5MB}")
    private String MAX_UPLOAD_SIZE;



    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?>badCredentials(AuthenticationException ex, final HttpServletRequest request) {
        var response = getErrorResponse(ex.getMessage(),HttpStatus.UNAUTHORIZED, request);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> appError(Model model, ApplicationException exception, final HttpServletRequest request){
        var response = getErrorResponse(exception.getMessage(),exception.getHttpStatus(), request);
        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        var response = getErrorResponse(errors.toString(),HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var response = getErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, final HttpServletRequest request) {
        var response = getErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex, final HttpServletRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            var response = getErrorResponse(ex.getMessage(),HttpStatus.CONFLICT, request);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return handleUnknownException(ex,request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleUnknownException(final Exception exception, final HttpServletRequest request) {
        HttpStatus status= getStatus(request);
        var response = getErrorResponse(exception.getMessage(),status, request);
        return new ResponseEntity<>(response, status);
    }



    @ExceptionHandler(ThymeleafException.class)
    public String thymeleafError(Model model, ThymeleafException exp){
        model.addAttribute("message", exp.getMessage());
        model.addAttribute("reason",exp.getReason());
        model.addAttribute("code", exp.getHttpStatus().value());
        return "error/error";
    }


    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception e) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    private ApiErrorResponse getErrorResponse(String message, HttpStatus status, final HttpServletRequest request){
        var guid = UUID.randomUUID().toString();
        log.error(String.format("Error GUID=%s; error message: %s", guid, message));
        return new ApiErrorResponse(
                guid,
                message,
                status.value(),
                status.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
    }

    private ApiErrorResponse getErrorResponse(String message, HttpStatus status, final WebRequest request){
        var guid = UUID.randomUUID().toString();
        log.error(String.format("Error GUID=%s; error message: %s", guid, message));
        return new ApiErrorResponse(
                guid,
                message,
                status.value(),
                status.name(),
                ((ServletWebRequest)request).getRequest().getRequestURI(),
                ((ServletWebRequest)request).getRequest().getMethod(),
                LocalDateTime.now()
        );
    }


}
