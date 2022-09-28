package com.mungta.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class UserApplication {

	public static ApplicationContext applicationContext;
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(UserApplication.class, args);
    try {
      log.info ("################ UserApplication Start !!!!!!!!!");
    } catch (NullPointerException e){
      log.error("error",e);
    }
	}
}
