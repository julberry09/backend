package com.mungta.user.kafka;

import com.mungta.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Slf4j
@Component
public class KafkaConsumer {

  @Autowired
  private UserService userService;

  @KafkaListener(topics = "accusation-topic", groupId ="com.example")
  public void AccusationCompleted(@Payload String payload) throws JsonMappingException, JsonProcessingException{
    log.debug("Kafka Accusation Consumed message = " + payload);
    var objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    var abstractEvent = objectMapper.readValue(payload, AbstractEvent.class);
    if(abstractEvent.getEventType().equals(new Object(){}.getClass().getEnclosingMethod().getName())){
      userService.givePenaltyUser(abstractEvent.getAccusedMemberId(),abstractEvent.getAccusationId());
    }
  }
}




