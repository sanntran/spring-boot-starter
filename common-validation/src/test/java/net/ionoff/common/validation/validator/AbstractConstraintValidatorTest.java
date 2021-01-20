package net.ionoff.common.validation.validator;

import net.ionoff.common.validation.DynamicConfiguration;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.junit.Before;
import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

import static org.mockito.Mockito.*;

public abstract class AbstractConstraintValidatorTest {

    @Mock
    protected DynamicConfiguration dynamicConfiguration;

    @Mock
    protected ConstraintValidatorContext validatorContext;

    @Mock
    protected HibernateConstraintValidatorContext hibernateValidatorContext;

    @Before
    public void setup() {
        when(validatorContext.unwrap(HibernateConstraintValidatorContext.class)).thenReturn(hibernateValidatorContext);
    }

    protected void verifyAddExpressionVariableFields(Collection<String> fields) {
        verify(hibernateValidatorContext, times(1)).addExpressionVariable("fields", fields);
    }

    protected void verifyAddMesssageParameter(String paramName, Object paramValue) {
        verify(hibernateValidatorContext, times(1)).addMessageParameter(paramName, paramValue);
    }
}
