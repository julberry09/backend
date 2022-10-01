package com.mungta.user.api;



import com.fasterxml.jackson.databind.ObjectMapper;

import com.mungta.user.dto.Token;
import com.mungta.user.dto.UserLoginDto;
import com.mungta.user.dto.UserResponseDto;
import com.mungta.user.model.UserEntity;
import com.mungta.user.service.AuthenticationService;
import com.mungta.user.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static com.mungta.user.sample.AuthenticationTestSample.*;
import static com.mungta.user.sample.UserTestSample.*;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity user;

    private Token token;

    private String userPasswordEncoding;

    @BeforeEach
    void setUp() {
        //한글 깨짐...
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();

        userPasswordEncoding = passwordEncoder.encode(USER_PASSWORD);

        user = UserEntity.builder()
                .userId(USER_ID)
                .userMailAddress(USER_MAIL_ADDRESS)
                .userPassword(userPasswordEncoding)
                .userName(USER_NAME)
                .userPhoto(USER_PHOTO)
                .userFileName(USER_FILE_NAME)
                .userFileOriName(USER_FILE_ORI_NAME)
                .userFileSize(USER_FILE_SIZE)
                .fileExtension(FILE_EXTENSION)
                .userTeamName(USER_TEAM_NAME)
                .userGender(USER_GENDER)
                .driverYn(DRIVER_YN)
                .settlementUrl(SETTLEMENT_URL)
                .carType(CAR_TYPE)
                .carNumber(CAR_NUMBER)
                .penaltyCount(PENALTY_COUNT)
                .status(USER_STATUS)
                .userType(USER_TYPE)
                .build();
    }
    @DisplayName("사용자 등록 (w/o pic) API")
    @Test
    void registerUser() throws Exception{
        doReturn(user)
                .when(userService).createUser(user);
        ResultActions resultActions = mockMvc.perform(
                post("/api/user/auth/signup/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                USER_REQUEST
                        ))
        );

        resultActions.andExpect(status().isOk());
    }
    @DisplayName("사용자 상세 정보 조회 API")
    @Test
    void getUser() throws Exception{
        doReturn(new UserResponseDto(user))
                .when(userService).getUser(USER_ID);

        ResultActions resultActions = mockMvc.perform(
                get("/api/user/"+USER_ID)
                        .header("userId", USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.userMailAddress").value(USER_MAIL_ADDRESS))
                .andExpect(jsonPath("$.userPhoto").value(USER_PHOTO))
                .andExpect(jsonPath("$.fileExtension").value(FILE_EXTENSION))
                .andExpect(jsonPath("$.userPassword").value(userPasswordEncoding))
                .andExpect(jsonPath("$.userName").value(USER_NAME))
                .andExpect(jsonPath("$.userFileName").value(USER_FILE_NAME))
                .andExpect(jsonPath("$.userFileOriName").value(USER_FILE_ORI_NAME))
                .andExpect(jsonPath("$.userFileSize").value(USER_FILE_SIZE))
                .andExpect(jsonPath("$.userTeamName").value(USER_TEAM_NAME))
                .andExpect(jsonPath("$.userGender").value(USER_GENDER))
                .andExpect(jsonPath("$.driverYn").value(DRIVER_YN.toString()))
                .andExpect(jsonPath("$.carType").value(CAR_TYPE))
                .andExpect(jsonPath("$.carNumber").value(CAR_NUMBER))
                .andExpect(jsonPath("$.penaltyCount").value(PENALTY_COUNT))
                .andExpect(jsonPath("$.status").value(USER_STATUS.toString()))
                .andExpect(jsonPath("$.userType").value(USER_TYPE.toString()));
    }




@DisplayName("사용자 수정")
@Test
void updateWoPhotoUser() throws Exception{
    doReturn(user)
            .when(userService).updateWoPhotoUser(user);
    ResultActions resultActions = mockMvc.perform(
                           put("/api/user/"+USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                USER_REQUEST
                    ))
    );

    resultActions.andExpect(status().isOk());
}

@DisplayName("사용자 탈퇴")
@Test
void deleteUser() throws Exception{
    doReturn(null)
            .when(userService).deleteUser(USER_ID);
    ResultActions resultActions = mockMvc.perform(
                                  delete("/api/user/"+USER_ID)
                                 .accept(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(
                                        USER_REQUEST
                    ))
    );
    resultActions.andExpect(status().isOk());
}



@DisplayName("사용자 패널티 부여")
@Test
void givePenaltyUser() throws Exception{
    doReturn(user)
            .when(userService).givePenaltyUser(USER_ID,"TEST");
    ResultActions resultActions = mockMvc.perform(
                           put("/api/user/penalty/"+USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("accusationId", "TEST")

    );

    resultActions.andExpect(status().isNoContent());
}



@DisplayName("로그인 API")
@Test
void authenticate() throws Exception{
    doReturn(token)
            .when(userService).getByCredentials(USER_ID,USER_PASSWORD);
    ResultActions resultActions = mockMvc.perform(
            post("/api/user/auth/signin")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new UserLoginDto(user)))
    );

    resultActions.andExpect(status().isOk());
}


@DisplayName("메일인증발송 API")
@Test
void sendEmailAuthNumber() throws Exception{
        doNothing()
            .when(authenticationService).sendAuthNumber(AUTH_USER_MAIL_ADDRESS);

    ResultActions resultActions = mockMvc.perform(
            post("/api/user/auth/mail")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(AUTH_REQUEST))
    );
    resultActions.andExpect(status().isOk());
}

}
