package com.interview.auth.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.interview.auth.model.UserInfoDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ValidationUtil {

	// Regex for checking complexity: 
	// (?=.*[A-Z])   -> must contain at least one uppercase letter
	// (?=.*[0-9])   -> must contain at least one digit
	// (?=.*[!@#$%^&*]) -> must contain at least one special character
	// .{8,20}       -> must be 8 to 20 characters long
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=\\S+$).{8,20}$";
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
	
	// ^[a-zA-Z0-9._%+-]+  : Start of string (^). Matches one or more (+) letters, digits, 
    //                        or allowed symbols (., _, %, +, -) for the username part.
    // @                      : Matches the literal '@' separator.
    // [a-zA-Z0-9.-]+       : Matches one or more letters, digits, periods (.), or hyphens (-) for the domain name.
    // \\.                    : Matches a literal dot (.), separating the domain from the TLD.
    // [a-zA-Z]{2,6}$         : Matches 2 to 6 letters for the Top-Level Domain (TLD) and ends the string ($).
	private static final String EMAIL_REGEX ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


	public Boolean validateUserAttributes(UserInfoDto userInfoDto) throws RuntimeException {
		if(!validateEmail(userInfoDto.getEmail())) {
			log.error("Invalid Email-Id.");
			throw new RuntimeException("Invalid Email-Id.");
		}
		if(!validatePassword(userInfoDto.getPassword())) {
			log.error("Invalid Password.");
			throw new RuntimeException("Invalid Password.");
		}
		return true;
	}
	
	public Boolean validateEmail(String email) throws RuntimeException{
		if(email == null || email.trim().isEmpty()) {
			log.error("Invalid Email-Id.");
			throw new RuntimeException("Invalid Email-Id");
		}
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.matches();
	}
	
	public Boolean validatePassword(String password) throws RuntimeException {
		if(password == null || password.length() < 8) {
			log.error("password should be atleast 8 characters long.");
			throw new RuntimeException("Insufficient characters length");
		}
		Matcher matcher = PASSWORD_PATTERN.matcher(password);
		return matcher.matches();
	}
}
