package com.epam.esm.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Min(value = 1, message = "page must be greater than 1")
@Digits(integer = 20, fraction = 0, message = "page number must consist of less then 20 digits")
@Constraint(validatedBy = {})
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPage {

    String message() default "invalid page";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
