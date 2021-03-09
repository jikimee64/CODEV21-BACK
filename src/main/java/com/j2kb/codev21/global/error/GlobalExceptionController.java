package com.j2kb.codev21.global.error;

import com.j2kb.codev21.domains.user.exception.MemberDuplicationException;
import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.j2kb.codev21.domains.user.exception.MemberNotFoundException;
import com.j2kb.codev21.global.common.ErrorResponse;
import com.j2kb.codev21.global.error.exception.InvalidValueException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionController {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> runtimeException(RuntimeException ex) {
        log.info("RuntimeException");
        ex.printStackTrace();
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .code("RunTime")
                .message("알수없는 런타임 에러입니다.")
                .status(000).build(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidValueException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidValueException(final InvalidValueException e) {
        log.error("handleInvalidValueException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("MethodArgumentNotValidException");
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .code(ErrorCode.VALIDATION.getCode())
                .message(builder.toString())
                .status(ErrorCode.VALIDATION.getStatus()).build(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MemberNotFoundException.class)
    public ResponseEntity<?> memberNotFoundException(MemberNotFoundException ex) {
        log.info("memberNotFoundException", ex);
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .code(ErrorCode.MEMBER_NOT_FOUND.getCode())
                .message(ErrorCode.MEMBER_NOT_FOUND.getMessage())
                .status(ErrorCode.MEMBER_NOT_FOUND.getStatus()).build(),
            HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = MemberDuplicationException.class)
    public ResponseEntity<?> memberDuplicationException(MemberDuplicationException ex) {
        log.info("memberDuplicationException", ex);
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .code(ErrorCode.EMAIL_DUPLICATION.getCode())
                .message(ErrorCode.EMAIL_DUPLICATION.getMessage())
                .status(ErrorCode.EMAIL_DUPLICATION.getStatus()).build(),
            HttpStatus.BAD_REQUEST);
    }

    //시큐리티 자체 에러
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        log.info("handleBadCredentialsException", ex);
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .code(ErrorCode.LOGIN_FAILED.getCode())
                .message(ErrorCode.LOGIN_FAILED.getMessage())
                .status(ErrorCode.LOGIN_FAILED.getStatus())
                .build(),
            HttpStatus.UNAUTHORIZED);
    }

    //인자값 불일치시 발생하는 스프링 자체 에러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("methodArgumentTypeMismatchException", ex);
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .code(ErrorCode.BAD_REQUEST.getCode())
                .message(ErrorCode.BAD_REQUEST.getMessage())
                .status(ErrorCode.BAD_REQUEST.getStatus())
                .build(),
            HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex) {
        log.info("accessDeniedException", ex);
        return new ResponseEntity<>(
            ErrorResponse.builder()
                .code(ErrorCode.ACCESS_DENIED.getCode())
                .message(ex.getMessage())
                .status(ErrorCode.ACCESS_DENIED.getStatus()).build(),
            HttpStatus.FORBIDDEN);
    }

}
