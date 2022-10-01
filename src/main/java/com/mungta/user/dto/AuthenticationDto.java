package com.mungta.user.dto;

import javax.validation.constraints.Email;

import com.mungta.user.model.AuthenticationEntity;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {

  @Email
	private String userMailAddress;
	private String authNumber;
  private String possibleYn;

  // Dto -> Entity
  public static AuthenticationEntity toEntity(final AuthenticationDto authDto){
    return AuthenticationEntity.builder()
                               .userMailAddress(authDto.getUserMailAddress())
                               .authNumber(authDto.getAuthNumber())
                               .build();
  }
}
