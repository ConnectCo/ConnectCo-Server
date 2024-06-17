package com.connectCo.global.validation.validator;

import com.connectCo.domain.organization.repository.OrganizationRepository;
import com.connectCo.domain.store.repository.StoreRepository;
import com.connectCo.global.validation.annotation.ExistOrganization;
import com.connectCo.global.validation.annotation.ExistStore;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationExistValidator implements ConstraintValidator<ExistOrganization, String> {

    private String errorMessage;
    private final OrganizationRepository organizationRepository;

    @Override
    public void initialize(ExistOrganization constraintAnnotation) {
        errorMessage = constraintAnnotation.message();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = !organizationRepository.existsOrganizationByName(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        }

        return isValid;
    }
}
