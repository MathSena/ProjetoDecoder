package com.ead.authuser.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameConstraintImpl.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraint {
  String message() default "Invalid Username";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
