package net.ionoff.service.validation.constraint;

import net.ionoff.service.validation.validator.ValidBookValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidBookValidator.class)
@Documented
public @interface ValidBook {

    String message() default "Author is not allowed {accept}";

    String accept() default "Ahihi";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
