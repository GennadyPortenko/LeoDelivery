package com.cmdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class OTPResponse {
    @Getter
    @Setter
    private Boolean sent = false;
}
