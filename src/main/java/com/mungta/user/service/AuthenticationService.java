package com.mungta.user.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mungta.user.api.ApiException;
import com.mungta.user.api.ApiStatus;
import com.mungta.user.dto.AuthenticationDto;
import com.mungta.user.model.AuthenticationEntity;
import com.mungta.user.model.AuthenticationRepository;
import com.mungta.user.model.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

	@Autowired
	private final AuthenticationRepository authenticationRepository;

	@Autowired
	private final UserRepository userRepository;

	@Autowired
  private final MailService mailService;

	private Random rand;


	//이메일 인증번호 발송
	@Transactional
  public void sendAuthNumber (final String email) throws ApiException {

		if(!isEmail(email)){
			throw new ApiException(ApiStatus.EMAIL_NOT_SK_ERROR);
		}
		if(userRepository.existsByUserMailAddress(email)){
			throw new ApiException(ApiStatus.DUPLICATED_INFORMATION);
		}

		//기존 이메일 인증 발송 정보 check
		if(authenticationRepository.existsByUserMailAddress(email)) {
			List<AuthenticationEntity> authlist = authenticationRepository.findByUserMailAddress(email);
			for(int i = 0; i < authlist.size(); i++){
				authlist.get(i).setUseYn("N");
				authenticationRepository.save(authlist.get(i));
			}
		}
		//인증정보 생성
		AuthenticationEntity auth = new AuthenticationEntity();
		String varCode ;
		try {
			//인증번호 GET
			rand = SecureRandom.getInstanceStrong();
			varCode = padLeftZeros(String.valueOf(rand.nextInt(9999999)), 7);
			//인증번호 Limit 시간설정(120sec)
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			cal.add(Calendar.SECOND, 300);
			String efDate = dateFormat.format(cal.getTime());

			//인증정보 SET
			auth.setUserMailAddress(email);
			auth.setAuthNumber(varCode);
			auth.setLimitTime(efDate);
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
		AuthenticationDto authDto = new AuthenticationDto(results);
		//현재시간 GET
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTime = dateFormat.format(Calendar.getInstance().getTime());
		log.debug("################ checkAuthNumber now-time   " + nowTime);
		log.debug("################ checkAuthNumber limit-time " + results.getLimitTime());
		log.debug("################ checkAuthNumber 비교       " + nowTime.compareTo(results.getLimitTime()));

		//인증번호 & 유효시간 check
		if(auth.getAuthNumber().equals(authDto.getAuthNumber()) && nowTime.compareTo(results.getLimitTime()) <= 0){

			authDto.setPossibleYn("Y");
		}else{
			authDto.setPossibleYn("N");
		}
		authDto.setAuthNumber("-");
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
		log.debug("################ StringUtils.hasText(email)   " + StringUtils.hasText(email));
    if( !StringUtils.hasText(email)){
        return false;
    }

    String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(email);
    if(m.matches() && email.toLowerCase().indexOf("@sk.") > 0) {
        validation = true;
    }

		log.debug("################ validation   " + validation);

    return validation;
	}

}

