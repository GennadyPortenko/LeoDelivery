package com.cmdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class LoginStatus {
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private String otp;
    @Getter
    @Setter
    Boolean authenticated;
    @Override
    public String toString() {
        return "phone : " + this.phone + ", otp : " + this.otp + ", authenticated : " + this.authenticated;
    }
}
