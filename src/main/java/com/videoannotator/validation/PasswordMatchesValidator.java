package com.videoannotator.validation;

import com.videoannotator.model.request.RegisterRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        final RegisterRequest user = (RegisterRequest) value;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
