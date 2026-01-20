package com.interview.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interview.auth.entities.RefreshToken;
import com.interview.auth.request.AuthRequestDTO;
import com.interview.auth.request.RefreshTokenRequestDTO;
import com.interview.auth.response.JwtResponseDTO;
import com.interview.auth.service.JwtService;
import com.interview.auth.service.RefreshTokenService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TokenController {
	
	 @Autowired
     AuthenticationManager authenticationManager;
	    @Autowired
	    RefreshTokenService refreshTokenService;
	    @Autowired
	    JwtService jwtService;	
	    


	@PostMapping("auth/v1/login")
	public ResponseEntity<?> AuthenticateandGenerateToken(@RequestBody AuthRequestDTO authRequestDto){
		// even though we have bypassed this api in filter (which bypasses the jwtfilter) but below line indirectly connects with spring security and uses the UserDetailsService.
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(),authRequestDto.getPassword()));
		if(authentication.isAuthenticated()) {
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
			return new ResponseEntity<>(JwtResponseDTO.builder()
													.accessToken(jwtService.generateToken(authRequestDto.getUsername()))
													.token(refreshToken.getToken())
													.build(),HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Exception while log-in",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("auth/v1/refreshToken")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenDto){
		log.info("received token object:"+refreshTokenDto);
		log.info("recieved token is :"+refreshTokenDto.getToken());
		return new ResponseEntity<>(refreshTokenService.findByToken(refreshTokenDto.getToken())
								  .map(refreshTokenService::verifyExpirations) // why we are writing :: means explained in the doc(PROJECT).
								  .map(RefreshToken::getUserInfo)
								  .map(userInfo -> {
									  String accessToken = jwtService.generateToken(userInfo.getUsername());
									  return JwtResponseDTO.builder() // if we return here it won't be the final return of the function it is just return to map and
											  				.accessToken(accessToken) // form as Optional<JwtResponseDTO>
											  				.token(refreshTokenDto.getToken())
											  				.build();
								  }).orElseThrow(()-> new RuntimeException("Refresh Token is not in DB.")),HttpStatus.OK);// this orElseThrow is the one which makes the final retunr
									// it will check whether the above map has null or not , if not it will return object if null throw exception.
		
	}
}
