
package net.ionoff.common.validation.constraints;

import net.ionoff.common.validation.validator.AllNullOrAllNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = AllNullOrAllNotNullValidator.class)

@Repeatable(AllNullOrAllNotNull.List.class)
@Documented
public @interface AllNullOrAllNotNull {


    String message() default "{net.ionoff.common.validation.constraints.AllNullOrAllNotNull.message}";

    String fields();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        AllNullOrAllNotNull[] value();
    }
}
