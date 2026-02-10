package org.nmascrie.swingy.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

/**
 * Validator for click-based inputs
 * Demonstrates validation for Menu 1 click options
 */
public class ClickValidator {
    private final Validator validator;

    public ClickValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
     * Validate that a click option is not null
     * You can add more complex validation rules as needed
     */
    public ValidationResult validateOption(String option) {
        ClickData data = new ClickData(option);
        Set<ConstraintViolation<ClickData>> violations = validator.validate(data);
        
        if (violations.isEmpty()) {
            return new ValidationResult(true, "Valid option: " + option);
        } else {
            ConstraintViolation<ClickData> violation = violations.iterator().next();
            return new ValidationResult(false, violation.getMessage());
        }
    }

    private static class ClickData {
        @NotNull(message = "Option cannot be null")
        private final String option;

        public ClickData(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
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