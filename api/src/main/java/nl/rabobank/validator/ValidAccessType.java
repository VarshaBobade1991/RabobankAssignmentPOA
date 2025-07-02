package nl.rabobank.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccessTypeValidator.class)
public @interface ValidAccessType {
    String message() default "Access type allowed values are: READ, WRITE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
