package com.interview.auth.util;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.auth.model.UserInfoDto;

public class UserInfoSerializer implements Serializer<UserInfoDto> {

	@Override
	public void configure(Map<String,?>configs , boolean iskey) {
		
	}
	@Override
	public byte[] serialize(String arg0, UserInfoDto arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
	@Override public void close() {
    }
	
}
