package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.ValidEmailAddressValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidEmailAddressValidator.class)
@Documented
public @interface ValidEmailAddress {

    String message() default "{net.ionoff.common.validation.constraints.ValidEmailAddress.message}";

    String regex() default "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[a-zA-Z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    String expression() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
