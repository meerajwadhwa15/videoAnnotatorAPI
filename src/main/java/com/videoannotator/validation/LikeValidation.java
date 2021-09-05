package com.videoannotator.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = LikeValidator.class)
@Documented
public @interface LikeValidation {
    String message() default "Cannot both like and dislike are true";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
