package com.mungta.user.model;

import lombok.Getter;

@Getter
public enum IsYN {
    Y(true),
    N(false);
    private boolean isYes;
    IsYN(boolean isYes){
        this.isYes=isYes;
    }
}
