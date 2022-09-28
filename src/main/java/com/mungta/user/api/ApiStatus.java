package com.mungta.user.api;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum  ApiStatus  {

    UNEXPECTED_ERROR      (HttpStatus.INTERNAL_SERVER_ERROR, -301  , "처리중 오류가 발생했습니다."  ),
    DUPLICATED_INFORMATION(HttpStatus.INTERNAL_SERVER_ERROR, -302  , "이미 존재하는 사용자입니다."  ),
    REQUIRED_INFORMATION  (HttpStatus.INTERNAL_SERVER_ERROR, -303  , "비밀번호는 필수 입력값입니다."),
    LOGIN_FAILED          (HttpStatus.INTERNAL_SERVER_ERROR, -304  , "로그인실패하였습니다."),
    EMAIL_ERROR          (HttpStatus.INTERNAL_SERVER_ERROR , -305  , "이메일발송이 실패했습니다."),
    NOT_EXIST_INFORMATION (HttpStatus.INTERNAL_SERVER_ERROR, -306  , "사용자가 존재하지 않습니다."  ),
    EMAIL_NOT_SK_ERROR          (HttpStatus.INTERNAL_SERVER_ERROR , -308  , "이메일형식이 올바르지 않습니다."),
    UNEXPECTED_PASSWORD  (HttpStatus.INTERNAL_SERVER_ERROR, -320  , "잘못된 비밀번호입니다."),
    NOT_DRIVER (HttpStatus.INTERNAL_SERVER_ERROR, -330  , "운전자로 등록되어 있지 않습니다."  ),;

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    ApiStatus(HttpStatus httpStatus, Integer code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
    }

    public static ApiStatus of(String message) {
        return Arrays.stream(ApiStatus.values())
                     .filter(apiStatus -> apiStatus.getMessage().equals(message))
                     .findFirst()
                     .orElse(null);
     }
}
