package com.Tejas.MovieTicketBookingSystem.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlleResourceNotFoundException(ResourceNotFoundException exception){
        logger.warn("Resource Not Found: {}",exception.getMessage());
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.name())
                .error(exception.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException exception){
        logger.warn("Unauthorized Access: {}",exception.getMessage());
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(HttpStatus.FORBIDDEN.name())
                .error(exception.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException exception){
        logger.warn("Invalid Input: {}",exception.getMessage());
        String error = exception.getBindingResult()
                .getFieldErrors()
                .stream().map(err->err.getField()+" : "+err.getDefaultMessage())
                .collect(Collectors.joining());

        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.name())
                .error(error)
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception){
        logger.warn("Username Not Found: {}",exception.getMessage());
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleJwtExpiredException(JwtExpiredException exception){
        logger.warn("Jwt Expired: {}",exception.getMessage());
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(HttpStatus.UNAUTHORIZED.name())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGlobalException(Exception ex){
        logger.error("Unexpected error occurred", ex);
        ExceptionResponse response = ExceptionResponse.builder()
                .timeStamp(LocalDateTime.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
