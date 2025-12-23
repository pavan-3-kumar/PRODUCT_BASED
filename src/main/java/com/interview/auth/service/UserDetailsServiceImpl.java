package com.interview.auth.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.interview.auth.Repo.UserRepository;
import com.interview.auth.entities.UserInfo;
import com.interview.auth.kafka_eventProducer.UserInfoProducer;
import com.interview.auth.model.UserInfoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService{
	 @Autowired
	    private  UserRepository userRepository;
	    // as this passwordencoder is an interface we have to create a bean in the config file.
	    @Autowired
	    private  PasswordEncoder passwordEncoder;
//	    @Autowired
//	    private ValidationUtil validationUtil;
	    
	    @Autowired
	    private UserInfoProducer userInfoProducer;
	    
	  
	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	    {
	        log.debug("Entering in loadUserByUsername Method...");
	    	UserInfo user = userRepository.findByUsername(username);
	    	if(user == null) {
	            log.error("Username not found: " + username);
	    		throw new UsernameNotFoundException("could not found user..!!");
	    	}
	        log.info("User Authenticated Successfully..!!!");
	    	return new CustomUserDetails(user);
	    }
	    
	    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto) {
	    	return userRepository.findByUsername(userInfoDto.getUsername());
	    }
	    
	    public Boolean signupUser(UserInfoDto userInfoDto) {
	    	// validate password , email
	    	// ValidationUtil.validateUserAttributes(userInfoDto);
//	    	validationUtil.validateUserAttributes(userInfoDto);
	    	userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
	    	log.info(userInfoDto.getUsername());
	    	if(Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))) {
	    		return false;
	    	}
	    	String userId = UUID.randomUUID().toString();
	    	 userRepository.save(new UserInfo(userId , userInfoDto.getUsername(),userInfoDto.getPassword(),new HashSet<>()));
	    	 userInfoDto.setUserId(userId);
	    	 // push event to QUEUE
	    	 userInfoProducer.sendEventToKafka(userInfoDto);
	    	 return true;
	    }
}
