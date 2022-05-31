package com.bobmate.bobmate.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiController {

    @ExceptionHandler({
            RuntimeException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity badRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                new BadRequestBody("code", "잘못된 접근 입니다.")
        );
    }

    @AllArgsConstructor
    @Getter
    static class BadRequestBody {
        private String code;
        private String message;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDenied(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new AccessDeniedBody("401", "Could not validate credentials")
        );

    }

    @AllArgsConstructor
    @Getter
    static class AccessDeniedBody {
        private String code;
        private String message;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity serverError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ServerErrorBody("500", "Internal server error")
        );
    }

    @AllArgsConstructor
    @Getter
    static class ServerErrorBody {
        private String code;
        private String message;
    }
}
