package com.mungta.user.dto;

import com.mungta.user.model.IsYN;
import lombok.NoArgsConstructor;

import com.mungta.user.model.Status;
import com.mungta.user.model.UserEntity;
import com.mungta.user.model.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
  private String userId;
  private String userMailAddress;
  private byte[] userPhoto;
  private String fileExtension;
  private String userPassword;
  private String userName;
  private String userFileName;
  private String userFileOriName;
  private long   userFileSize;
  private String userTeamName;
  private String userGender;
  private IsYN driverYn;
  private String settlementUrl;
  private String carType;
  private String carNumber;
  private Long   penaltyCount;
  private Status status;
  private UserType userType;

  // Entity -> Dto
  public UserResponseDto(final UserEntity user){
    this.userId          = user.getUserId();
    this.userMailAddress = user.getUserMailAddress();
    this.userPhoto       = user.getUserPhoto();
    this.fileExtension   = user.getFileExtension();
    this.userPassword    = user.getUserPassword();
    this.userName        = user.getUserName();
    this.userFileName    = user.getUserFileName ();
    this.userFileOriName = user.getUserFileOriName();
    this.userFileSize    = user.getUserFileSize();
    this.userTeamName    = user.getUserTeamName();
    this.userGender      = user.getUserGender();
    this.driverYn        = user.getDriverYn();
    this.settlementUrl   = user.getSettlementUrl();
    this.carType         = user.getCarType();
    this.carNumber       = user.getCarNumber();
    this.penaltyCount    = user.getPenaltyCount();
    this.userType        = user.getUserType();
    this.status          = user.getStatus();
  }
}
