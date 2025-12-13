package com.interview.auth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interview.auth.entities.UserInfo;

import lombok.Data;

@JsonNaming (PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class UserInfoDto extends UserInfo{
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	
}
