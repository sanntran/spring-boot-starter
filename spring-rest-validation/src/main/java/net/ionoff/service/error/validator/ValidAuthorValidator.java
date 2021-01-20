package net.ionoff.service.error.validator;

import com.mkyong.api.model.AuthorDto;
import net.ionoff.service.error.DynamicConfig;
import net.ionoff.common.validation.validator.AbstractValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;

@Component
public class ValidAuthorValidator extends AbstractValidator implements ConstraintValidator<ValidAuthor, AuthorDto> {

    private DynamicConfig dynamicConfig;

    @Autowired
    public ValidAuthorValidator(DynamicConfig dynamicConfig) {
        this.dynamicConfig = dynamicConfig;
    }

    @Override
    public boolean isValid(AuthorDto author, ConstraintValidatorContext context) {
        if (author.getName() == null) {
            return true;
        }
        if (dynamicConfig.isValid(author.getName())) {
            return true;
        }
        addFieldsVariable(context, Collections.singleton("name"));
        return false;
    }

}
