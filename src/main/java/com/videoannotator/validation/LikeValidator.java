package com.videoannotator.validation;

import com.videoannotator.model.request.UserLikeRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LikeValidator implements ConstraintValidator<LikeValidation, UserLikeRequest> {
    @Override
    public void initialize(LikeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserLikeRequest value, ConstraintValidatorContext context) {
        return !(value.isDislike() && value.isLike());
    }
}
