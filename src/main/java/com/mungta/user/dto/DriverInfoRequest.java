package com.mungta.user.dto;

import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverInfoRequest {
    private String settlementUrl;
    private String carType;
    private String carNumber;

    public static DriverInfoRequest of(String settlementUrl, String carType, String carNumber){
        return DriverInfoRequest.builder()
                .settlementUrl(settlementUrl)
                .carType(carType)
                .carNumber(carNumber)
                .build();
    }
}
