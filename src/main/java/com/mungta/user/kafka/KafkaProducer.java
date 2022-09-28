package com.mungta.user.kafka;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.cloud.stream.function.StreamBridge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final StreamBridge streamBridge;

    @Async
    public void send(AbstractEvent payload){
      log.debug("Kafka Producer send Message = " + payload);

      streamBridge.send("producer-out-0", MessageBuilder
              .withPayload(payload)
              .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
              .build());
    }



}



