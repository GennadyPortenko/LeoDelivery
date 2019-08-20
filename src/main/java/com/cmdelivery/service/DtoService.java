package com.cmdelivery.service;

import org.springframework.stereotype.Service;

@Service
public class DtoService {
    public static String parsePhone(String phone) {
        return phone.replaceAll("[^\\d.]", "").substring(1);
    }

    public static String toMaskedPhone(String phone) {
        return "+7 (" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6, 10);
    }
}
