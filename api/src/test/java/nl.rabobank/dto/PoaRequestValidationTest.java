package nl.rabobank.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static nl.rabobank.constants.PoaConstants.READ;
import static nl.rabobank.constants.PoaConstants.SAVINGS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PoaRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldFailWhenGrantorIsBlank() {
        PoaRequest request = new PoaRequest();
        request.setGrantor(""); // Invalid
        request.setGrantee("Bob");
        request.setAccountNumber("123456789");
        request.setAccountType(SAVINGS);
        request.setAccessType(READ);

        Set<ConstraintViolation<PoaRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("grantor")));
    }

}
