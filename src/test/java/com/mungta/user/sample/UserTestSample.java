package com.mungta.user.sample;

import com.mungta.user.dto.UserDto;
import com.mungta.user.model.IsYN;
import com.mungta.user.model.Status;
import com.mungta.user.model.UserType;

public class UserTestSample {

    public static final String USER_ID="TEST01";
    public static final String USER_MAIL_ADDRESS = "TEST01@test.com";
    public static final String USER_PASSWORD="TEST01";
    public static final String USER_NAME="TESTIKIM";
     public static final String USER_FILE_NAME = "TEST01.png";
    public static final String USER_FILE_ORI_NAME = "TEST.png";
    public static final long   USER_FILE_SIZE =0L;
    public static final String FILE_EXTENSION = "png";
    public static final String USER_TEAM_NAME="TeamName";
    public static final String USER_GENDER="M";

    public static final IsYN DRIVER_YN=IsYN.Y;
    public static final String SETTLEMENT_URL="https://kakao.com";
    public static final String CAR_TYPE="BMW";
    public static final String CAR_NUMBER="77A7777";
    public static final Long PENALTY_COUNT =0L;
    public static final Status USER_STATUS = Status.ACTIVATED;
    public static final UserType USER_TYPE = UserType.CUSTOMER;
   // public static final String STR = "photo";
    public static final byte[] USER_PHOTO = null ;
    //STR.getBytes();
    // {0x48, 0x65, (byte)0x6C, (byte)0x6C, (byte)0x6f,
	// 	0x20, 0x57, (byte)0x6f, 0x72, (byte)0x6c, 0x64};


    public static final UserDto USER_REQUEST;

    static {USER_REQUEST = UserDto.builder()
                            .userId(USER_ID)
                            .userPassword(USER_PASSWORD)
                            .userMailAddress(USER_MAIL_ADDRESS)
                            .userName(USER_NAME)
                            .userFileName(USER_FILE_NAME)
                            .userFileOriName(USER_FILE_NAME)
                            .userFileSize(USER_FILE_SIZE)
                            .userTeamName(USER_TEAM_NAME)
                            .userGender(USER_GENDER)
                            .driverYn(DRIVER_YN)
                            .settlementUrl(SETTLEMENT_URL)
                            .carType(CAR_TYPE)
                            .carNumber(CAR_NUMBER)
                            .penaltyCount(0L)
                            .status(USER_STATUS)
                            .userType(USER_TYPE)
                            .build();
    }

}
