package com.mungta.user.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="AUTHENTICATION", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class AuthenticationEntity  extends BaseEntity {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

  @Email
	private String userMailAddress;
	private String authNumber;
	private String limitTime;

  @Builder.Default
  private String useYn ="Y" ;

  public static AuthenticationEntity of(String userMailAddress, String authNumber, String limitTime, String useYn){
    return AuthenticationEntity.builder()
                               .userMailAddress(userMailAddress)
                               .authNumber(authNumber)
                               .limitTime(limitTime)
                               .useYn(useYn)
                               .build();
  }
}
