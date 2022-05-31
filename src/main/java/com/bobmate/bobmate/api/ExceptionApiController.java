package com.bobmate.bobmate.api;

import com.bobmate.bobmate.exception.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionApiController {

    @AllArgsConstructor
    @Getter
    static class GeneralExceptionDto {
        private String code;
        private String message;
    }

    @ExceptionHandler({
            BookmarkDuplicateException.class,
            DeletedMemberException.class,
            DeletedPlaceException.class,
            DeletedReviewException.class,
            EmailDuplicateException.class,
            FollowDuplicateException.class,
            HeadMemberException.class,
            LikeDuplicateException.class,
            MemberMeetDuplicateException.class,
            StarValueException.class,
            TagBookmarkDuplicateException.class,
            TagBookmarkMemberException.class,
            TagNameDuplicateException.class
    })
    public ResponseEntity badRequest(RuntimeException ex) {
        // 400
        log.warn("error", ex);
        return ResponseEntity.badRequest().body(
                new GeneralExceptionDto("400", "Bad request, " +
                        ex.getMessage())
        );
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDenied(RuntimeException ex) {
        // 401
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new GeneralExceptionDto("401", "Could not validate credentials")
        );
    }


//    @ExceptionHandler
    public ResponseEntity forbidden(Exception ex) {
        // 403
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new GeneralExceptionDto("403", "Unauthorized speaker")
        );
    }


    @ExceptionHandler({
            NullPointerException.class
    })
    public ResponseEntity notFound(Exception ex) {
        // 404
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new GeneralExceptionDto("404", "Data does not exist")
        );
    }


//    @ExceptionHandler
    public ResponseEntity unprocessable(Exception ex) {
        // 422
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new GeneralExceptionDto("422", "Unprocessable Entity\t\n" +
                        "Unable to process request (in normal cases, validation failed)")
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity serverError(Exception ex) {
        // 500
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralExceptionDto("500", "Internal server error")
        );
    }
}
