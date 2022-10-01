package com.mungta.user.model;

import org.junit.jupiter.api.BeforeEach;
import static com.mungta.user.sample.UserTestSample.*;

import java.time.LocalDateTime;
public class UserTest {

  private UserEntity userEntity;

  @BeforeEach
  void setUp() {
    userEntity = UserEntity.builder()
    .userId(USER_ID)
    .userPassword(USER_PASSWORD)
    .userMailAddress(USER_MAIL_ADDRESS)
    .userName(USER_NAME)
    .userFileName(USER_FILE_NAME)
    .userFileOriName(USER_FILE_NAME)
    .userFileSize(USER_FILE_SIZE)
    .userTeamName(USER_TEAM_NAME)
    .userGender(USER_GENDER)
    .driverYn(IsYN.Y)
    .settlementUrl(SETTLEMENT_URL)
    .carType(CAR_TYPE)
    .carNumber(CAR_NUMBER)
    .penaltyCount(0L)
    .status(Status.ACTIVATED)
    .userType(UserType.CUSTOMER)
    .build();
   userEntity.setUpdateDttm(LocalDateTime.now());
  }
}
