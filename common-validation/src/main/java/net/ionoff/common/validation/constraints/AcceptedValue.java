package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.AcceptedValueCollectionValidator;
import net.ionoff.common.validation.validator.AcceptedValueStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {AcceptedValueStringValidator.class, AcceptedValueCollectionValidator.class})
@Documented
public @interface AcceptedValue {

    String message() default "{net.ionoff.common.validation.constraints.AcceptedValue.message}";

    String value() default "";

    String expression() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
