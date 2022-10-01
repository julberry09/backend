package com.mungta.user.model;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, String>{

   Optional <UserEntity> findByUserId(String userId);
	UserEntity findByUserMailAddress(String userMailAddress);
   UserEntity findByUserIdOrUserMailAddress(String userId,String userMailAddress);
   UserEntity findByUserIdAndUserPassword(String userId,String userPassword);
   List<UserEntity> findAll();
   Boolean existsByUserId(String userId);
	Boolean existsByUserMailAddress(String userMailAddress);

}
