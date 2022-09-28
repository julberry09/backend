package com.mungta.user.api;

import lombok.Getter;

public class ApiException extends RuntimeException {
  @Getter
  private ApiStatus apiStatus;

  public ApiException(ApiStatus apiStatus) {
    super(apiStatus.getMessage());
    this.apiStatus = apiStatus;
  }

  public ApiException(ApiStatus apiStatus, Throwable e) {
      super(apiStatus.getMessage(), e);
      this.apiStatus = apiStatus;
  }
}



