package nl.rabobank.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

import static nl.rabobank.constants.PoaConstants.READ;
import static nl.rabobank.constants.PoaConstants.WRITE;

public class AccessTypeValidator implements ConstraintValidator<ValidAccessType, String> {

    private static final Set<String> ALLOWED = Set.of(READ.toLowerCase(), WRITE.toLowerCase());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && ALLOWED.contains(value.toLowerCase());
    }
}
