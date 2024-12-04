package net.ionoff.service.validation.validator;

import net.ionoff.service.validation.constraint.ValidBookType;
import net.ionoff.spring.api.model.BookTypeDto;
import net.ionoff.service.error.DynamicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
public class ValidBookTypeValidator implements ConstraintValidator<ValidBookType, List<BookTypeDto>> {

    @Autowired
    private DynamicConfig dynamicConfig;

    @Override
    public boolean isValid(List<BookTypeDto> bookTypes, ConstraintValidatorContext context) {
        if (bookTypes == null) {
            return true;
        }
        return bookTypes.size() > 3;
    }
}
