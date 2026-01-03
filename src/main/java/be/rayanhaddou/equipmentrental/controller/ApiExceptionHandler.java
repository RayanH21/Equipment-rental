package be.rayanhaddou.equipmentrental.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> forbidden(AccessDeniedException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> conflict(DataIntegrityViolationException ex) {
        // bv. duplicate email door unique constraint
        return ResponseEntity.status(409).body("Conflict");
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> optimisticConflict(OptimisticLockingFailureException ex) {
        // bv. stock werd net door iemand anders aangepast
        return ResponseEntity.status(409).body("Stock changed, please try again.");
    }
}