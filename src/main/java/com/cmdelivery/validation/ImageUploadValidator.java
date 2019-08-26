package com.cmdelivery.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ImageUploadValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> allowedExtensions = new HashSet<>(Arrays.asList("png", "jpg", "jpeg", "bmp", "gif"));
        String name = image.getName();
        String extension = name.substring(name.lastIndexOf('.') + 1);
        for (String allowedExtension : allowedExtensions) {
            if (allowedExtension.equalsIgnoreCase(extension))
                return true;
        }
        return false;
    }

}
