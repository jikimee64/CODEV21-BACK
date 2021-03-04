package com.j2kb.codev21.global.error;

import com.j2kb.codev21.domains.user.exception.MemberNotFoundException;
import com.j2kb.codev21.global.common.CommonResponse;
import com.j2kb.codev21.global.common.ErrorResponse;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
                .message(ex.getMessage())
                .status(ErrorCode.MEMBER_NOT_FOUND.getStatus()).build(),
            HttpStatus.FORBIDDEN);
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
