package com.bobmate.bobmate.api;

import com.bobmate.bobmate.exception.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class,
            HttpMediaTypeNotSupportedException.class // 요구되는 미디어타입이 아닐경우
    })
    public ResponseEntity badRequest(Exception ex) {
        // 400
        log.warn("error : ", ex);
        return ResponseEntity.badRequest().body(
                new GeneralExceptionDto("400", "Bad request, " +
                        ex.getMessage())
        );
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDenied(Exception ex) {
        // 401
        log.warn("error : ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new GeneralExceptionDto("401", "Could not validate credentials")
        );
    }


//    @ExceptionHandler
    public ResponseEntity forbidden(Exception ex) {
        // 403
        log.warn("error : ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new GeneralExceptionDto("403", "Unauthorized speaker")
        );
    }


    @ExceptionHandler({
            NullPointerException.class
    })
    public ResponseEntity notFound(Exception ex) {
        // 404
        log.warn("error : ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new GeneralExceptionDto("404", "Data does not exist")
        );
    }


    @ExceptionHandler({
            BookmarkDuplicateException.class,
            UserNameDuplicateException.class,
            FollowDuplicateException.class,
            LikeDuplicateException.class,
            MemberMeetDuplicateException.class,
            TagBookmarkDuplicateException.class,
            TagNameDuplicateException.class,
            DataIntegrityViolationException.class // DB에서 유니크제한조건 불만족시
    })
    public ResponseEntity conflict(Exception ex) {
        // 409
        log.warn("error : ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new GeneralExceptionDto("409", "자원이 중복됩니다, " +
                        ex.getMessage())
        );
    }


    @ExceptionHandler({
            DeletedMemberException.class,
            DeletedPlaceException.class,
            DeletedReviewException.class,
            HeadMemberException.class,
            StarValueException.class,
            TagBookmarkMemberException.class,
    })
    public ResponseEntity unprocessable(Exception ex) {
        // 422
        log.warn("error : ", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new GeneralExceptionDto("422", "Unprocessable Entity\t\n" +
                        "Unable to process request (in normal cases, validation failed), " +
                        ex.getMessage())
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity serverError(Exception ex) {
        // 500
        log.warn("error : ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralExceptionDto("500", "Internal server error" +
                        ", " + ex.toString())
        );
    }
}
