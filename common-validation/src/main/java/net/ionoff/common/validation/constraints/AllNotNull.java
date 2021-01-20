
package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.AllNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = AllNotNullValidator.class)
@Documented
@Repeatable(AllNotNull.List.class)
public @interface AllNotNull {

    String message() default "{net.ionoff.common.validation.constraints.AllNotNull.message}";

    String fields();

    String dependOn() default "";

    String condition() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        AllNotNull[] value();
    }
}
