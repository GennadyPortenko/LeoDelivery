package com.cmdelivery.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
// OTP - one time password
public class OTPService { //cache based on username and OPT MAX 8
    private static final Integer EXPIRE_MINS = 1;
    private LoadingCache<String, Integer> otpCache;

    public OTPService() {
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public int generateOTP(String phone) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(phone, otp);
        return otp;
    }

    public int getOtp(String phone) {
        try{
            return otpCache.get(phone);
        } catch (Exception e) {
            return 0;
        }
    }

    //This method is used to clear the OTP catched already
    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }

}
