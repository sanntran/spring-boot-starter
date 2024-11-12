package net.ionoff.service.validation.validator;

import net.ionoff.service.error.DynamicConfig;
import net.ionoff.service.validation.constraint.ValidAuthorName;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

@Component
public class ValidAuthorNameValidator implements ConstraintValidator<ValidAuthorName, String> {

    ValidAuthorName constraintAnnotation;

    @Autowired
    private DynamicConfig dynamicConfig;

    @Override
    public void initialize(ValidAuthorName constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }


    @Override
    public boolean isValid(String author, ConstraintValidatorContext context) {
        try {
            Field field = context.getClass().getField("basePath");
            field.setAccessible(true);
            System.out.println(field.get(context));
            //
        } catch (Exception e) {
            //
        }
        context.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("accept", "HIHIHIHII");
        System.out.println(constraintAnnotation.code());
        return dynamicConfig.isValid(author);

    }
}
