package com.mungta.user.kafka;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class PenaltyFailed extends AbstractEvent {

  public PenaltyFailed(String accusedMemberId, String accusationId) {
      super();
      this.eventType = this.getClass().getSimpleName();
      this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      this.accusedMemberId = accusedMemberId;
      this.accusationId = accusationId;
  }

  @Override
  public String toString() {
      return "PenaltyFailed{" +
              "eventType='" + eventType + '\'' +
              ", timestamp='" + timestamp + '\'' +
              ", accusedMemberId='" + accusedMemberId + '\'' +
              ", accusationId='" + accusationId + '\'' +
              '}';
  }
}
