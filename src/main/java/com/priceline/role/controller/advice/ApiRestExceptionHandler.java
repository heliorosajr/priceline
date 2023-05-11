package com.priceline.role.controller.advice;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.priceline.role.model.error.PricelineApiError;
import com.priceline.role.model.exception.PricelineApiException;

@ControllerAdvice
public class ApiRestExceptionHandler extends ResponseEntityExceptionHandler {

    // HttpRequestMethodNotSupportedException
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                     HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final StringBuilder builder = new StringBuilder("Supported method(s) are: ");
        builder.append(ex.getSupportedHttpMethods().stream().map(String::valueOf).collect(Collectors.joining(",")));

        PricelineApiException exception = new PricelineApiException("Method not supported", ex.getMessage(),
                builder.toString(), HttpStatus.valueOf(status.value()));
        PricelineApiError error = new PricelineApiError(exception, request);

        return new ResponseEntity<>(error, headers, error.getStatus());
    }

    // HttpMediaTypeNotSupportedException
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                     HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder builder = new StringBuilder("Supported media type(s) are: ");
        builder.append(ex.getSupportedMediaTypes().stream().map(String::valueOf).collect(Collectors.joining(",")));

        PricelineApiException exception = new PricelineApiException("Media Type not supported", ex.getMessage(),
                builder.toString(), HttpStatus.valueOf(status.value()));
        PricelineApiError error = new PricelineApiError(exception, request);

        return new ResponseEntity<>(error, headers, error.getStatus());
    }

    // PricelineApiException
    @ExceptionHandler(PricelineApiException.class)
    public ResponseEntity<Object> handleRealStateApiException(final PricelineApiException ex, final WebRequest request) {
    	PricelineApiError error = new PricelineApiError(ex, request);

        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }

    // Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleGenericException(final Exception ex, final WebRequest request) {
    	PricelineApiException exception = new PricelineApiException("Unexpected error", ex.getMessage(),
                null, HttpStatus.INTERNAL_SERVER_ERROR);
    	PricelineApiError error = new PricelineApiError(exception, request);

        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                          HttpStatusCode statusCode, WebRequest request) {
    	PricelineApiException exception = new PricelineApiException("Unexpected error", ex.getMessage(),
                null, HttpStatus.valueOf(statusCode.value()));
    	PricelineApiError error = new PricelineApiError(exception, request);

        return new ResponseEntity<>(error, headers, error.getStatus());
    }

}