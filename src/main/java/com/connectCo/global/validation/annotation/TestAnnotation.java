package com.connectCo.global.validation.annotation;

import com.connectCo.global.validation.validator.TestAnnotationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TestAnnotationValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    String message() default "error 키워드 사용은 금지입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
