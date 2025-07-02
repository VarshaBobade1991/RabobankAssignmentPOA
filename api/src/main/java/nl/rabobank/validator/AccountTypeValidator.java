package nl.rabobank.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

import static nl.rabobank.constants.PoaConstants.PAYMENTS;
import static nl.rabobank.constants.PoaConstants.SAVINGS;

public class AccountTypeValidator implements ConstraintValidator<ValidAccountType, String> {

    private static final Set<String> ALLOWED = Set.of(SAVINGS.toLowerCase(), PAYMENTS.toLowerCase());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && ALLOWED.contains(value.toLowerCase());
    }
}
