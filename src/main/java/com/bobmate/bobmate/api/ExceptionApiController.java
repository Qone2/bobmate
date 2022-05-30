package com.bobmate.bobmate.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionApiController {

    public ResponseEntity badRequest(final RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    public ResponseEntity accessDenied(final RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    static class AccessDeniedBody {
        private String code;
        private String message;
    }
}
