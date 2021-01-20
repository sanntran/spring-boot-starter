package net.ionoff.service.error.validator;

import com.mkyong.api.model.BookDto;
import net.ionoff.service.error.DynamicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@Component
public class ValidBookValidator implements CustomValidator<ValidBook, BookDto> {

    @Autowired
    private DynamicConfig dynamicConfig;

    @Override
    public void initialize(ValidBook constraintAnnotation) {
        register(this);
    }

    @Override
    public boolean isValid(BookDto author, ConstraintValidatorContext context) {
        return dynamicConfig.isValid(author.getName());

    }

    @Override
    public String enrichMessage(BookDto rejectedAuthor, String defaultMessage) {
        return MessageFormat.format(defaultMessage, rejectedAuthor.getName());
    }
}
