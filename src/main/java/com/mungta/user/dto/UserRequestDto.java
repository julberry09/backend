package com.mungta.user.dto;

import java.io.Serializable;

import com.mungta.user.model.IsYN;
import org.springframework.web.multipart.MultipartFile;

import com.mungta.user.model.UserEntity;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto  implements Serializable {

  private String userId;
  private String userPassword;
  private String userMailAddress;
  private String userName;
  private String userTeamName;
  private String userGender;
  private IsYN driverYn;
  private String settlementUrl;
  private String carType;
  private String carNumber;
  private MultipartFile profileImg;

  // Entity -> Dto
  public UserRequestDto(final UserEntity user){
    this.userId          = user.getUserId();
    this.userPassword    = user.getUserPassword();
    this.userMailAddress = user.getUserMailAddress();
    this.userName        = user.getUserName();
    this.userTeamName    = user.getUserTeamName();
    this.userGender      = user.getUserGender();
    this.driverYn        = user.getDriverYn();
    this.settlementUrl   = user.getSettlementUrl();
    this.carType         = user.getCarType();
    this.carNumber       = user.getCarNumber();

  }
  // Dto -> Entity
  public static UserEntity toEntity(final UserRequestDto userRequestDto){
    return UserEntity.builder()
               .userId(userRequestDto.getUserId())
               .userPassword(userRequestDto.getUserPassword())
               .userMailAddress(userRequestDto.getUserMailAddress())
               .userName(userRequestDto.getUserName())
               .userTeamName(userRequestDto.getUserTeamName())
               .userGender(userRequestDto.getUserGender())
               .driverYn(userRequestDto.getDriverYn())
               .settlementUrl(userRequestDto.getSettlementUrl())
               .carType(userRequestDto.getCarType())
               .carNumber(userRequestDto.getCarNumber())
               .build();
  }
}
