package com.mungta.user.service;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import com.mungta.user.dto.UserDto;
import com.mungta.user.dto.UserResponseDto;
import com.mungta.user.model.IsYN;
import com.mungta.user.model.Status;
import com.mungta.user.model.UserEntity;
import com.mungta.user.model.UserRepository;
import com.mungta.user.model.UserType;

import lombok.extern.slf4j.Slf4j;

import static org.mockito.Mockito.verify;

import static com.mungta.user.sample.UserTestSample.*;


@Slf4j
@ExtendWith(value = MockitoExtension.class)
class UserServiceTests {

    @InjectMocks
    @Spy
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private UserEntity user;

    private final LocalDateTime nowDateTime = LocalDateTime.now();



    @BeforeEach
    void setUp() {

      user = UserEntity.builder()
      .userId(USER_ID)
      .userPassword(USER_PASSWORD)
      .userMailAddress(USER_MAIL_ADDRESS)
      .userName(USER_NAME)
      .fileExtension(FILE_EXTENSION)
      .userFileName(USER_FILE_NAME)
      .userFileOriName(USER_FILE_NAME)
      .userFileSize(USER_FILE_SIZE)
      .userPhoto(USER_PHOTO)
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
      user.setCreatedDttm(nowDateTime);
      user.setUpdateDttm(nowDateTime);
    }

    @DisplayName("사용자조회")
    @Test
    void getUser() {
      given(userRepository.findByUserId(USER_ID))
      .willReturn(Optional.ofNullable(user));
      UserResponseDto userComp = userService.getUser(USER_ID);
      assertAll(
    ()->assertThat(userComp.getUserId()).isEqualTo(USER_ID));
    }



}
