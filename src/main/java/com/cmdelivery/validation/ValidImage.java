package com.cmdelivery.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageUploadValidator.class)
@Target( { ElementType.PARAMETER } )
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {

    String message() default "Wrong image format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
