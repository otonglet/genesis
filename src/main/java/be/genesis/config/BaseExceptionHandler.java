package be.genesis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity handleInvalidRequest(NoSuchElementException ex) {
        log.info("404", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
