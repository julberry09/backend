package com.mungta.user.exception;

import lombok.Getter;

public class ApiException extends RuntimeException {
  @Getter
  private final ApiStatus apiStatus;

  public ApiException(ApiStatus apiStatus) {
    super(apiStatus.getMessage());
    this.apiStatus = apiStatus;
  }

  public ApiException(ApiStatus apiStatus, Throwable e) {
      super(apiStatus.getMessage(), e);
      this.apiStatus = apiStatus;
  }
}



