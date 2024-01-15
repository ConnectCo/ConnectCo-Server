package com.connectCo.global.validation.validator;

import com.connectCo.global.validation.annotation.TestAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestAnnotationValidator implements ConstraintValidator<TestAnnotation, String> {

    private String errorMessage;
    @Override
    public void initialize(TestAnnotation constraintAnnotation) {
        errorMessage = constraintAnnotation.message();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {

        boolean isValid = !s.equals("error");

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        }

        return isValid;
    }
}
