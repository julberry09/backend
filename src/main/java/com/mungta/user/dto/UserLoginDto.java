package com.mungta.user.dto;

import com.mungta.user.model.UserEntity;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

  private String userId;
  private String userPassword;

  public UserLoginDto (UserEntity user){
    this.userId          = user.getUserId();
    this.userPassword    = user.getUserPassword();

  }

  public static UserEntity toEntity(final UserLoginDto userLoginDto){
    return UserEntity.builder()
               .userId(userLoginDto.getUserId())
               .userPassword(userLoginDto.getUserPassword())
               .build();
  }
}
