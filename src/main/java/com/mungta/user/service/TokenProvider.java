package com.mungta.user.service;

import com.mungta.user.dto.Token;
import com.mungta.user.model.UserEntity;
import java.util.Base64;
import java.util.Date;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
@Service
public class TokenProvider {

	private static final long ACCESS_TOKEN_EXPIRE_TIME  = 1000L * 60 * 30;            // 0.5 hr
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;   // 7 days

	private static final String secretKey = "MungTa03Service";
	String encodedKey  = Base64.getEncoder().encodeToString(secretKey.getBytes());

	public Token createToken (UserEntity user) {

		Date now = new Date();

	 return Token.builder().accessToken(makeAccessToken(now,user)).refreshToken(makeRefreshToken(now,user)).key(user.getUserId()).build();
	}

	public String makeRefreshToken(Date now,UserEntity user){

		Claims claims = Jwts.claims()
				.setIssuer("mungtacarpool.com")
				.setSubject(user.getUserId());

		//claims for refresh
		claims.put("userId"  ,user.getUserId());

		return Jwts.builder()
				.signWith(SignatureAlgorithm.HS256, encodedKey)
				.setHeaderParam("typ", "JWT")
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
				.compact();
	}
	public String makeAccessToken(Date now, UserEntity user){

		Claims claims = Jwts.claims()
				.setIssuer("mungtacarpool.com")
				.setSubject(user.getUserId());

		//claims for refresh
		claims.put("userId"  ,user.getUserId());
		claims.put("name"    ,user.getUserName());
		claims.put("email"   ,user.getUserMailAddress());
		claims.put("team"    ,user.getUserTeamName());
		claims.put("userType",user.getUserType());
		claims.put("userGender", user.getUserGender());
		claims.put("driverYn",user.getDriverYn());

		return  Jwts.builder()
				.signWith(SignatureAlgorithm.HS256,encodedKey)
				.setHeaderParam("typ", "JWT")
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
				.compact();
	}
}

