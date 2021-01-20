
package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.ValidDateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Documented
public @interface ValidDateRange {


    String message() default "{net.ionoff.common.validation.constraints.ValidDateRange.message}";

    String fromDate();

    String toDate();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
