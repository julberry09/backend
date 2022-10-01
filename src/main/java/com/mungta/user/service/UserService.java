package com.mungta.user.service;

import com.mungta.user.dto.*;
import com.mungta.user.exception.ApiException;
import com.mungta.user.exception.ApiStatus;
import com.mungta.user.kafka.KafkaProducer;
import com.mungta.user.kafka.PenaltyFailed;
import com.mungta.user.kafka.PenaltySucceed;
import com.mungta.user.model.Status;
import com.mungta.user.model.UserEntity;
import com.mungta.user.model.UserRepository;
import com.mungta.user.model.UserType;

import java.util.Date;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

	@Autowired
	private final UserRepository userRepository;

	@Autowired
  private final TokenProvider tokenProvider;

	@Autowired
  private final PasswordEncoder passwordEncoder;

	@Autowired
  private final StorageService storageService;

	private final KafkaProducer producer;

	//사용자정보 조회
	@Transactional
	public UserResponseDto getUser (final String userId) {
		UserEntity results = userRepository.findByUserId(userId)
	                     	.orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));
		return new UserResponseDto(results);
  }
	//사용자정보 조회(w 사진)
	@Transactional
	public UserResponseDto getUserPhoto (final String userId) {
		UserEntity results = userRepository.findByUserId(userId)
																.orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));
		return new UserResponseDto(results);
  }

	//사용자정보 등록
	@Transactional
	public UserEntity  createUser (@Valid final UserEntity user) {
		if(userRepository.existsByUserMailAddress(user.getUserMailAddress())) {
			log.warn("Email already exists {}", user.getUserMailAddress());
			throw new ApiException(ApiStatus.DUPLICATED_INFORMATION);
		}
		try{
			user.setUserPassword(encodePassword(user.getUserPassword()));
			userRepository.save(user);

		} catch(Exception e){
			log.error("error created user",user.getUserId(),e);
			throw new ApiException(ApiStatus.UNEXPECTED_ERROR);
		}
    return null;
	}

	//사용자정보 등록(사진)
	@Transactional
	public String createUserWithPhoto (@Valid final UserEntity user, MultipartFile profileImg) {
		//Email 중복 체크
		if(userRepository.existsByUserMailAddress(user.getUserMailAddress())) {
			log.warn("Email already exists {}", user.getUserMailAddress());
			throw new ApiException(ApiStatus.DUPLICATED_INFORMATION);
		}
		String fileName ="";
		try{
			user.setUserPassword(encodePassword(user.getUserPassword()));

			FileInfo fileInfo = storageService.store(user.getUserId(),profileImg);
			user.setUserPhoto(IOUtils.toByteArray(profileImg.getInputStream()));
			user.setUserFileName(fileInfo.getUserFileName());
			user.setUserFileOriName(fileInfo.getUserFileOriName());
			user.setUserFileSize(profileImg.getSize());
			user.setFileExtension(fileInfo.getFileExtension());

			//사용자정보 저장
			userRepository.save(user);
			fileName=fileInfo.getUserFileName();

		} catch(Exception e){
			log.error("error created user",user.getUserId(),e);
			throw new ApiException(ApiStatus.UNEXPECTED_ERROR);
		}
    return fileName;
	}

	//사용자정보 변경
	@Transactional
	public UserEntity updateUser (@Valid final UserEntity user, MultipartFile profileImg) {

		UserEntity userUp = userRepository.findByUserId(user.getUserId())
		                  .orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));
		try{
			if(user.getUserPassword().isEmpty()){
				user.setUserPassword(userUp.getUserPassword());
			}else if(!matchesPassword(user.getUserPassword(),userUp.getUserPassword())){
					String enPw = encodePassword(user.getUserPassword());
					user.setUserPassword(enPw);
			}
			if(profileImg.isEmpty()){
				user.setUserPhoto(userUp.getUserPhoto());
				user.setUserFileName(userUp.getUserFileName());
				user.setUserFileOriName(userUp.getUserFileOriName());
				user.setUserFileSize(userUp.getUserFileSize());
				user.setFileExtension(userUp.getFileExtension());
			}else{
			FileInfo fileInfo = storageService.store(user.getUserId(),profileImg);
			user.setUserPhoto(IOUtils.toByteArray(profileImg.getInputStream()));
			user.setUserFileName(fileInfo.getUserFileName());
			user.setUserFileOriName(fileInfo.getUserFileOriName());
			user.setUserFileSize(profileImg.getSize());
			user.setFileExtension(fileInfo.getFileExtension());
			}
			userRepository.save(user);
		} catch(Exception e){
			log.error("error updating user",user.getUserId(),e);
			throw new ApiException(ApiStatus.UNEXPECTED_ERROR);
		}
		return null;
	}
	//사용자정보 변경
	@Transactional
	public UserEntity updateWoPhotoUser (@Valid final UserEntity user) {

		UserEntity userUp = userRepository.findByUserId(user.getUserId())
		                  .orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));
		try{
			if(!user.getUserPassword().isEmpty() && !matchesPassword(user.getUserPassword(),userUp.getUserPassword())){
					String enPw = encodePassword(user.getUserPassword());
					userUp.setUserPassword(enPw);
			}
			userUp.setUserName(user.getUserName());
			userUp.setUserTeamName(user.getUserTeamName());
			userUp.setUserGender(user.getUserGender());
			userUp.setDriverYn(user.getDriverYn());
			userUp.setSettlementUrl(user.getSettlementUrl());
			userUp.setCarType(user.getCarType());
			userUp.setCarNumber(user.getCarNumber());
			userRepository.save(userUp);
		} catch(Exception e){
			log.error("error updating user",user.getUserId(),e);
			throw new ApiException(ApiStatus.UNEXPECTED_ERROR);
		}
		return null;
	}
	//사용자정보삭제
	@Transactional
	public UserEntity deleteUser (final String userId) {

		if(!userRepository.existsByUserId(userId)) {
			log.warn("No UserId", userId);
			throw new ApiException(ApiStatus.NOT_EXIST_INFORMATION);
		}
		UserEntity user = new UserEntity();
		user.setUserId(userId);
		try{
			userRepository.delete(user);
		} catch(Exception e){
			log.error("error deleting user",user.getUserId(),e);
			throw new ApiException(ApiStatus.UNEXPECTED_ERROR);
		}
		return null;
	}

	//사용자 패널티 부과
	@Transactional
	public UserEntity givePenaltyUser (final String userId, final String accusationId){
		UserEntity user = new UserEntity();
		try{
			user = userRepository.findByUserId(userId)
												.orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));

			// 패널티 횟수에 따라서 사용자 상태 변경
			if(user.getPenaltyCount() < 3){
				user.setPenaltyCount(user.getPenaltyCount()+1);
				user.setStatus(Status.ACTIVATED);
			}else{
				user.setStatus(Status.BANNED);
			}
			// 패널티 정보  업데이트
			userRepository.save(user);
		} catch(Exception e){
			log.error("error accusing user",user.getUserId(),e);
			producer.send( new PenaltyFailed(userId,accusationId));
			return null;
		}
		producer.send( new PenaltySucceed(userId,accusationId));
		return null;
	}

	//사용자 정상 로그인시 인증정보 GET
	public Token getByCredentials(final String userId, final String userPassword) {
		UserEntity userIdInfo = userRepository.findByUserIdOrUserMailAddress(userId,userId);
		UserEntity results = userRepository.findByUserId(userIdInfo.getUserId())
		.orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));

		if(matchesPassword(userPassword,results.getUserPassword())){
			//토큰 생성
			Token issuedToken = tokenProvider.createToken(results);
			return issuedToken ;
		}else{
			throw new ApiException(ApiStatus.UNEXPECTED_PASSWORD);
		}
	}

	public String tokenRefresh(String userId) {
		UserEntity results = userRepository.findByUserId(userId)
				.orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));
		return tokenProvider.makeAccessToken(new Date(), results);
	}

	//사용자정보 전체조회 (ADMIN)
	@Transactional
	public List<UserDto> findAll() {
		//요청한 사용자가 관리자가 아닐경우 오류처리
		return userRepository.findAll().stream()
					.map(UserDto::new)
					.collect(Collectors.toList());
	}

	//관리자사용자로 변경 (ADMIN)
	@Transactional
	public UserEntity chageUserType (final String userId) {
		//해당 아이디 조회
		UserEntity user = userRepository.findByUserId(userId)
	                     	.orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION));
		try{
			user.setUserType(UserType.ADMIN);
			userRepository.save(user);
		} catch(Exception e){
			log.error("error changing user type",user.getUserId(),e);
			throw new ApiException(ApiStatus.UNEXPECTED_ERROR);
		}
		return null;
	}

	//비밀번호 암호화
	private String encodePassword (final String pwdBf){
		if(pwdBf == ""){
			throw new ApiException(ApiStatus.REQUIRED_INFORMATION);
		}
		String pwdAf = passwordEncoder.encode(pwdBf);
		return pwdAf;
	}

	//비밀번호 암호화값 일치 여부 check
	private Boolean matchesPassword (final String pwdInput, final String pwdDb){
		if(passwordEncoder.matches(pwdInput, pwdDb)){
			return true;
		}else{
			return false;
		}
	}

	public DriverInfoRequest getDriverInfo(String userId){
		return userRepository.findByUserId(userId)
				.orElseThrow(()-> new ApiException(ApiStatus.NOT_EXIST_INFORMATION))
				.getDriverInfo();
	}

	public Boolean checkIdDuplicate(String userId){
		return userRepository.findByUserId(userId)
				.isPresent();
	}

}
