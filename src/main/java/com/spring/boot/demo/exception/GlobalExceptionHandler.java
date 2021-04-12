package com.spring.boot.demo.exception;

import com.spring.boot.demo.controller.EventController;
import com.spring.boot.demo.exception.bean.EventNotFoundException;
import com.spring.boot.demo.exception.bean.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(assignableTypes = EventController.class)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processException(Exception ex){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse("INTERNAL_SERVER_ERROR", "Unexpected error occured");
        return new ResponseEntity<>(exceptionResponse, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processNotFoundException(EventNotFoundException ex){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, responseHeaders, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processResponseStatusException(ResponseStatusException ex){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getReason(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, responseHeaders, ex.getStatus());
    }


    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse( "BAD_REQUEST",ex.getMessage());
        return this.handleExceptionInternal(ex, exceptionResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse( "BAD_REQUEST",ex.getMessage());
        return this.handleExceptionInternal(ex, exceptionResponse, headers, status, request);
    }
}
