package com.interview.auth.kafka_eventProducer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.interview.auth.model.UserInfoDto;

@Service
public class UserInfoProducer {
//		@Autowired
//		private 
		KafkaTemplate<String,UserInfoDto> kafkaTemplate;
		
		@Value("${custom.kafka.topic.user-created}")
		private String topicName;
		
		public UserInfoProducer(KafkaTemplate<String , UserInfoDto> kafkaTempalte){
			this.kafkaTemplate = kafkaTempalte;
		}
		
		
		public void sendEventToKafka(UserInfoDto userInfoDto) {
//		  Message<UserInfoDto> message  = MessageBuilder.withPayload(userInfoDto)
//				  										.setHeader(KafkaHeaders.TOPIC, topicName)
//				  										.build();
//		  kafkaTemplate.send(message);
			System.out.println("Attempting to send to topic: " + topicName + " Data: " + userInfoDto);

	        // Using simpler send method with a callback for debugging
	        kafkaTemplate.send(topicName, userInfoDto).whenComplete((result, ex) -> {
	            if (ex == null) {
	                System.out.println("SENT SUCCESS: Sent message to [" + topicName + "] partition: " + result.getRecordMetadata().partition());
	            } else {
	                System.err.println("SENT ERROR: Failed to send message: " + ex.getMessage());
	                ex.printStackTrace();
	            }
	        });
		}

}
