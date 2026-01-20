package com.interview.auth.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interview.auth.entities.RefreshToken;
import com.interview.auth.model.UserInfoDto;
import com.interview.auth.response.JwtResponseDTO;
import com.interview.auth.service.JwtService;
import com.interview.auth.service.RefreshTokenService;
import com.interview.auth.service.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	JwtService jwtService;
	
	
	
	@PostMapping("auth/v1/signup")
	public ResponseEntity<?> singUp(@RequestBody UserInfoDto userInfoDto) {
		try {
			// need to add the user , if user is not there add him or throw exception.
			Boolean isUserSignedup = userDetailsService.signupUser(userInfoDto);
			if(!isUserSignedup) {
				return new ResponseEntity<>("Already User Exist.",HttpStatus.BAD_GATEWAY);
			}
			// if user is there generate the refreshtoken and jwt/access token.
			RefreshToken token = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
			String jwtToken = jwtService.generateToken(userInfoDto.getUsername());
			return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).token(token.toString()).build(),HttpStatus.OK);
		}catch(Exception ex) {
			 return new ResponseEntity<>("Exception in User Service",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/auth/v1/ping")
    public ResponseEntity<String> ping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = userDetailsService.getUserByUsername(authentication.getName());
            if(Objects.nonNull(userId)){
                return ResponseEntity.ok(userId);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }
}
