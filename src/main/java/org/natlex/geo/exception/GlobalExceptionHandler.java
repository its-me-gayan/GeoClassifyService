package org.natlex.geo.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.natlex.geo.dto.generic.GenericResponse;
import org.natlex.geo.helper.ResponseGenerator;
import org.natlex.geo.util.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/12/24
 * Time: 1:57 PM
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResponseGenerator responseGenerator;
    /**
     * Constraint violation exception handler
     *
     * @param ex ConstraintViolationException
     * @return List<ValidationError> - list of ValidationError built
     * from set of ConstraintViolation
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<String> errorList = new ArrayList<>();
        constraintViolations.forEach(constraintViolation -> {
            errorList.add("parameter "+constraintViolation.getPropertyPath().toString()+" "+ (StringUtils.hasText(constraintViolation.getMessage()) ?constraintViolation.getMessage() : null));
        });

        GenericResponse requestProcessingFailed = responseGenerator.generateErrorExceptionResponse(
                ResponseMessages.REQUEST_PROC_FAILED,
                ResponseMessages.REQUEST_PROC_FAILED_DUE_VAL,
                HttpStatus.BAD_REQUEST,
                errorList
        );
        return ResponseEntity.status(requestProcessingFailed.getHttpStatusCode()).body(requestProcessingFailed);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // Collect validation errors
        List<String> errorList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorList.add("field "+error.getField()+" "+error.getDefaultMessage())
        );

        GenericResponse requestProcessingFailed = responseGenerator.generateErrorExceptionResponse(
                ResponseMessages.REQUEST_PROC_FAILED,
                ResponseMessages.REQUEST_PROC_FAILED_DUE_VAL,
                HttpStatus.BAD_REQUEST,
                errorList
        );
        return ResponseEntity.status(requestProcessingFailed.getHttpStatusCode()).body(requestProcessingFailed);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GenericResponse> handleEntityNotFountException(EntityNotFoundException ex){
        GenericResponse requestProcessingFailed = responseGenerator.generateErrorExceptionResponse(
                ResponseMessages.REQUEST_PROC_FAILED,
                ex.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.status(requestProcessingFailed.getHttpStatusCode()).body(requestProcessingFailed);
    }

    @ExceptionHandler(EntityExistException.class)
    public ResponseEntity<GenericResponse> handleEntityExistException(EntityExistException ex){
        GenericResponse requestProcessingFailed = responseGenerator.generateErrorExceptionResponse(
                ResponseMessages.REQUEST_PROC_FAILED,
                ex.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.status(requestProcessingFailed.getHttpStatusCode()).body(requestProcessingFailed);
    }

    @ExceptionHandler(NoContentFoundException.class)
    public ResponseEntity<GenericResponse> handleEntityExistException(NoContentFoundException ex){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<GenericResponse> handleValidationFailedException(ValidationFailedException ex){
        GenericResponse requestProcessingFailed = responseGenerator.generateErrorExceptionResponse(
                ResponseMessages.REQUEST_PROC_FAILED,
                ex.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.status(requestProcessingFailed.getHttpStatusCode()).body(requestProcessingFailed);
    }

    @ExceptionHandler(FileExportFailedException.class)
    public ResponseEntity<GenericResponse> handleValidationFailedException(FileExportFailedException ex){
        GenericResponse requestProcessingFailed = responseGenerator.generateErrorExceptionResponse(
                ResponseMessages.REQUEST_PROC_FAILED,
                ex.getLocalizedMessage(),
                HttpStatus.BAD_GATEWAY
        );
        return ResponseEntity.status(requestProcessingFailed.getHttpStatusCode()).body(requestProcessingFailed);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleCommonException(Exception ex){
        GenericResponse requestProcessingFailed = responseGenerator.generateErrorExceptionResponse(
                ResponseMessages.REQUEST_PROC_FAILED,
                ex.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.status(requestProcessingFailed.getHttpStatusCode()).body(requestProcessingFailed);
    }
}
