package com.mungta.user.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.mungta.user.dto.AuthenticationDto;
import com.mungta.user.exception.ApiException;
import com.mungta.user.exception.ApiStatus;
import com.mungta.user.model.AuthenticationEntity;
import com.mungta.user.model.AuthenticationRepository;
import com.mungta.user.model.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

	@Autowired
	private final AuthenticationRepository authenticationRepository;

	@Autowired
	private final UserRepository userRepository;

	@Autowired
  private final MailService mailService;

	//이메일 인증번호 발송
	@Transactional
  public void sendAuthNumber (final String email) throws ApiException {
		// 이메일 형식 체크
		if(!isEmail(email)){
			throw new ApiException(ApiStatus.EMAIL_FORMAT_ERROR);
		}
		if(Boolean.TRUE.equals(userRepository.existsByUserMailAddress(email))){
			throw new ApiException(ApiStatus.DUPLICATED_INFORMATION);
		}
		// 유효한 인증번호 사용여부 변경
		if(Boolean.TRUE.equals(authenticationRepository.existsByUserMailAddress(email))) {
			AuthenticationEntity authOld = authenticationRepository.findByUserMailAddressAndUseYn(email,"Y");
			authOld.setUseYn("N");
			authenticationRepository.save(authOld);
		}
		//인증정보 생성
		AuthenticationEntity auth = new AuthenticationEntity();
		Random rand;
		String varCode ;
		try {
			//인증번호 GET
			rand    = SecureRandom.getInstanceStrong();
			varCode = padLeftZeros(String.valueOf(rand.nextInt(9999999)), 7);
			//인증번호 Limit 시간설정(300sec)
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 300);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

			//인증정보 SET
			auth = AuthenticationEntity.builder()
																 .userMailAddress(email)
																 .authNumber(varCode)
																 .limitTime(dateFormat.format(cal.getTime()))
																 .build();
			authenticationRepository.save(auth);

			// 이메일 발송
			String subject ="[MungtaCarpool] Verification code for signup";
			String text    ="Thank you for joining us. please use this verification code: "+ varCode;
			mailService.mailSend(email,subject,text);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}


	//이메일 인증번호 확인
	@Transactional
  public AuthenticationDto checkAuthNumber (final AuthenticationEntity auth) {
		//유효한 인증정보 조회
		AuthenticationEntity results = authenticationRepository.findByUserMailAddressAndUseYn(auth.getUserMailAddress(),"Y");
		String possibleYn = "";
		//현재시간 GET
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTime = dateFormat.format(Calendar.getInstance().getTime());


		//인증번호 & 유효시간 check
		if(auth.getAuthNumber().equals(results.getAuthNumber()) && nowTime.compareTo(results.getLimitTime()) <= 0){

			possibleYn = "Y";
		}else{
			possibleYn = "N";
		}
		AuthenticationDto authDto = AuthenticationDto.builder()
		.userMailAddress(results.getUserMailAddress())
		.authNumber("-")
		.possibleYn(possibleYn)
		.build();

		return authDto;
	}

	//인증번호 padding
	public String padLeftZeros(final String inputString, final int length) {
    if (inputString.length() >= length) {
        return inputString;
    }
    StringBuilder sb = new StringBuilder();
    while (sb.length() < length - inputString.length()) {
        sb.append('0');
    }
    sb.append(inputString);
    return sb.toString();
	}
	//이메일 형식 체크
	public static boolean isEmail(String email){

    boolean validation = false;
    if( !StringUtils.hasText(email)){
        return false;
    }

    String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(email);
    if(m.matches()){
        validation = true;
    }
    return validation;
	}
}

