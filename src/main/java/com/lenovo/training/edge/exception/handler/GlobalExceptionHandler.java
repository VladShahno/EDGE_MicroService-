package com.lenovo.training.edge.exception.handler;

import com.lenovo.training.edge.exception.ImportCsvException;
import com.lenovo.training.edge.exception.ResourceExistsException;
import com.lenovo.training.edge.exception.ResourceNotFoundException;
import com.lenovo.training.edge.payload.response.ExceptionResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    Map<String, Object> details = new LinkedHashMap<>();

    public ResponseEntity<ExceptionResponse> handleException(
        Exception exception, WebRequest webRequest, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(String.valueOf(httpStatus.value()))
            .error(httpStatus.getReasonPhrase())
            .message(exception.getMessage())
            .path(webRequest.getDescription(false).substring(4))
            .timestamp(OffsetDateTime.now(ZoneId.systemDefault()))
            .details(details)
            .build();
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(value = {ResourceExistsException.class})
    public ResponseEntity<ExceptionResponse> handleClientExceptions(
        RuntimeException runtimeException, WebRequest request) {
        return handleException(runtimeException, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(
        ResourceNotFoundException resourceNotFoundException, WebRequest request) {
        return handleException(resourceNotFoundException, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ImportCsvException.class})
    public ResponseEntity<ExceptionResponse> handleImportCsvException(
        ImportCsvException validException, WebRequest request) {
        return handleException(validException, request, HttpStatus.BAD_REQUEST);
    }
}