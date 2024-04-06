package ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.Payload;
import jakarta.validation.Constraint;

import ru.jankbyte.spring.oauth2.authorizationserver.validation.validator.UsernameExistsValidator;

@Documented
@Retention(RUNTIME)
@Target({ PARAMETER, TYPE_PARAMETER, TYPE_USE, FIELD })
@Constraint(validatedBy = { UsernameExistsValidator.class })
public @interface IsUsernameExists {
    String message() default "Account with name still exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
