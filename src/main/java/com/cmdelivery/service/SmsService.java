package com.cmdelivery.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    public void sendOTP(int otp) {
        System.out.println(otp);
    }
}
