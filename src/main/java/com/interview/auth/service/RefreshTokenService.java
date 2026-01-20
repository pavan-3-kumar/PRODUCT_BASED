package com.interview.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.auth.Repo.RefreshTokenRepository;
import com.interview.auth.Repo.UserRepository;
import com.interview.auth.entities.RefreshToken;
import com.interview.auth.entities.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RefreshTokenService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken createRefreshToken(String username) {
		UserInfo userInfoExtracted =  userRepository.findByUsername(username);
		RefreshToken refreshToken = RefreshToken.builder()
												.userInfo(userInfoExtracted)
												.expiryDate(Instant.now().plusMillis(Duration.ofHours(5).plusMinutes(40).toMillis()))
												.token(UUID.randomUUID().toString())
												.build();
		return refreshTokenRepository.save(refreshToken);
	}
	
	public Optional<RefreshToken> findByToken(String token){
		    log.info("given token is :"+token);
			List<RefreshToken> all = refreshTokenRepository.findAll();
			for(RefreshToken i : all) {
				log.info(i+"\n");
			}
		   Optional<RefreshToken> resToken = refreshTokenRepository.findByToken(token);
//		   log.info("refresh token from DB : "+resToken.get().getToken());
			return resToken;
	}
	
	public RefreshToken verifyExpirations(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
		}
		return token;
	}

}
