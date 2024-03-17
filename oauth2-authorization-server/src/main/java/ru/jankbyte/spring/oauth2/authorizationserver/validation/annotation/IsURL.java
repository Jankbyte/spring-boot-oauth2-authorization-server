package ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.Payload;
import jakarta.validation.Constraint;

@Constraint(validatedBy = {})
@Target({ PARAMETER, TYPE_PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Pattern(regexp = "^(http|https):\\/\\/(\\w+(\\.\\w+)*)+(:\\d+)?(\\/([\\w\\d\\/,=#%?&.-]+)?)?$",
    message = "{url.invalid}"
)
public @interface IsURL {
    String message() default "{url.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
