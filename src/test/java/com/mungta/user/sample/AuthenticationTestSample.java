package com.mungta.user.sample;

import com.mungta.user.dto.AuthenticationDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuthenticationTestSample {

    public static final long AUTH_ID = 1L;

    public static final String AUTH_USER_MAIL_ADDRESS = "test01@test.com";
    public static final String AUTH_NUMBER = "012345";
    public static final String LIMIT_TIME= LocalDateTime.now().plusSeconds(300).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

    public static final String AUTH_USE_YN ="Y" ;


    public static final AuthenticationDto AUTH_REQUEST;

    static {AUTH_REQUEST = AuthenticationDto.builder()
                            .userMailAddress(AUTH_USER_MAIL_ADDRESS)
                            .authNumber(AUTH_NUMBER)
                            .possibleYn(AUTH_USE_YN)
                            .build();
    }
}
