package com.mungta.user.config;

import com.mungta.user.api.ApiException;
import com.mungta.user.api.ApiStatus;
import com.mungta.user.api.MessageEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
