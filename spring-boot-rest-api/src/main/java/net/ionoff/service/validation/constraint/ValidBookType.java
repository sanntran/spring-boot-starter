package net.ionoff.service.validation.constraint;

import net.ionoff.service.validation.validator.ValidBookTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidBookTypeValidator.class)
@Documented
public @interface ValidBookType {

    String message() default "{ValidBookType}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
