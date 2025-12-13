package com.interview.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.auth.Repo.RefreshTokenRepository;
import com.interview.auth.Repo.UserRepository;
import com.interview.auth.entities.RefreshToken;
import com.interview.auth.entities.UserInfo;

@Service
public class RefreshTokenService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken createRefreshToken(String username) {
		UserInfo userInfoExtracted =  userRepository.findByUsername(username);
		RefreshToken refreshToken = RefreshToken.builder()
												.userInfo(userInfoExtracted)
												.expiryDate(Instant.now().plusMillis(600000))
												.token(UUID.randomUUID().toString())
												.build();
		return refreshTokenRepository.save(refreshToken);
	}
	
	public Optional<RefreshToken> findByToken(String token){
			return refreshTokenRepository.findByToken(token);
	}
	
	public RefreshToken verifyExpirations(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
		}
		return token;
	}

}
