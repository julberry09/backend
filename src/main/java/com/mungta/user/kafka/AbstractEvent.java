package com.mungta.user.kafka;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;


@Getter
@Setter
@NoArgsConstructor
public class AbstractEvent {

  @JsonProperty("eventType")
  String eventType;

  @JsonProperty("timestamp")
  String timestamp;

  @JsonProperty("accusedMemberId")
  String accusedMemberId;

  @JsonProperty("accusationId")
  String accusationId;
}
