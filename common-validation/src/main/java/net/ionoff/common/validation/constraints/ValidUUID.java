package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.ValidUUIDArrayValidator;
import net.ionoff.common.validation.validator.ValidUUIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ValidUUIDValidator.class, ValidUUIDArrayValidator.class})
@Documented
public @interface ValidUUID {

    String message() default "{net.ionoff.common.validation.constraints.ValidUUID.message}";

    boolean acceptNull() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
