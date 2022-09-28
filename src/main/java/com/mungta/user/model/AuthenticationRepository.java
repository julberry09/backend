package com.mungta.user.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository <AuthenticationEntity, String>{

  List<AuthenticationEntity> findByUserMailAddress(String userMailAddress);
  AuthenticationEntity findByUserMailAddressAndUseYn(String userMailAddress,String useYn);
  Boolean existsByUserMailAddress(String userMailAddress);

}
