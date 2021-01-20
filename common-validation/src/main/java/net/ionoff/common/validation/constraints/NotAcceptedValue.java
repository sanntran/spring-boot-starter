package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.NotAcceptedValueCollectionValidator;
import net.ionoff.common.validation.validator.NotAcceptedValueNumberValidator;
import net.ionoff.common.validation.validator.NotAcceptedValueStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {NotAcceptedValueNumberValidator.class,
        NotAcceptedValueStringValidator.class,
        NotAcceptedValueCollectionValidator.class
})
@Documented
public @interface NotAcceptedValue {

    String message() default "{net.ionoff.common.validation.constraints.NotAcceptedValue.message}";

    String value() default "";

    String expression() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
