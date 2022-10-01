package com.mungta.user.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<MessageEntity> handleApiException(ApiException e) {
        ApiStatus apiStatus = e.getApiStatus();
        return new ResponseEntity<>(MessageEntity.of(apiStatus),apiStatus.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<MessageEntity> handleException(Exception e) {
        ApiStatus apiStatus = ApiStatus.UNEXPECTED_ERROR;
        return new ResponseEntity<>(MessageEntity.of(apiStatus),apiStatus.getHttpStatus());
    }
}
