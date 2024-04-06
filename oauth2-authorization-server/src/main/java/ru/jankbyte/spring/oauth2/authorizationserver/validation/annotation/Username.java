package ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.Payload;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Pattern;

@Documented
@Retention(RUNTIME)
@Target({ PARAMETER, TYPE_PARAMETER, TYPE_USE, FIELD })
@Constraint(validatedBy = {})
@Pattern(regexp = "^[\\w]{4,}$")
public @interface Username {
    String message() default "Invalid username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
