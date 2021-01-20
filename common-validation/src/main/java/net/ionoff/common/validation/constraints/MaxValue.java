package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.MaxValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = MaxValueValidator.class)
@Documented
public @interface MaxValue {

    String message() default "{net.ionoff.common.validation.constraints.MaxValue.message}";

    int value() default 0;

    String expression() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
