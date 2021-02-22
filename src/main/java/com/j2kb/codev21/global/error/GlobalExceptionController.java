package com.j2kb.codev21.global.error;

import com.j2kb.codev21.global.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            CommonResponse.builder()
                .code("RunTime")
                .message("알수없는 런타임 에러입니다.")
                .data("none").build(),
            HttpStatus.BAD_REQUEST);
    }

}
