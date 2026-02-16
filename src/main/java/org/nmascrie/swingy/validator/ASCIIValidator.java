package org.nmascrie.swingy.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Validator class for validating the inputs.
 */
public class ASCIIValidator {
    private final Validator validator;

    public ASCIIValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public ValidationResult validate(String input) {
        InputData data = new InputData(input);
        Set<ConstraintViolation<InputData>> violations = validator.validate(data);
        
        if (violations.isEmpty()) {
            return new ValidationResult(true, input);
        } else {
            return new ValidationResult(false, "Invalid input! Only V, K, U, D, L or R allowed.");
        }
    }

    private static class InputData {
        @Size(max=1, min=1)
        @Pattern(regexp = "[VKUDLRvkudlr1234567890YyNnZzPpLlQqAa]", message = "Invalid input! Only V, K, U, D, L or R allowed.")
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
