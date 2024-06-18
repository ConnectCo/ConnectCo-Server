package com.connectCo.global.validation.annotation;

import com.connectCo.global.validation.validator.OrganizationExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OrganizationExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistOrganization {
    String message() default "존재하는 조직명입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
