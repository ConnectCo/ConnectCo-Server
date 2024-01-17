package com.connectCo.global.validation.annotation;

import com.connectCo.global.validation.validator.StoreExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StoreExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistStore {
    String message() default "존재하는 가게명입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
