package org.nmascrie.swingy.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;

/**
 * Validator class for validating the inputs.
 */
public class NameValidator {
    private final Validator validator;

    public NameValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public ValidationResult validate(String input) {
        InputData data = new InputData(input);
        Set<ConstraintViolation<InputData>> violations = validator.validate(data);
        
        if (violations.isEmpty()) {
            return new ValidationResult(true, input);
        } else {
            return new ValidationResult(false, "Invalid input! Only letters, digits and space are allowed.");
        }
    }

    private static class InputData {
        @Pattern(regexp = "[A-Za-z0123456789 ]", message = "Invalid input! Only letters, digits and space are allowed.")
        private final String key;

        public InputData(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
