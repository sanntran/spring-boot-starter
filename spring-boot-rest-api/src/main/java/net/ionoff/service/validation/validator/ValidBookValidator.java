package net.ionoff.service.validation.validator;

import lombok.AllArgsConstructor;
import net.ionoff.service.error.DynamicConfig;
import net.ionoff.service.validation.constraint.ValidBook;
import net.ionoff.spring.api.model.BookDto;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class ValidBookValidator implements ConstraintValidator<ValidBook, BookDto> {

    private DynamicConfig dynamicConfig;
    
    @Override
    public boolean isValid(BookDto author, ConstraintValidatorContext context) {
        return dynamicConfig.isValid(author.getName());

    }
}
