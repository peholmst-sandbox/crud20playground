package org.vaadin.playground.crud20.data.property.validation;

import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyValidatorTest {

    @Test
    void validators_are_not_automatically_triggered_on_the_initial_value() {
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property).withValidator(new StringLengthValidator("error", 1, 10));
        {
            assertThat(validator.hasError().value()).isFalse();
            assertThat(validator.result().value()).isEmpty();
            assertThat(validator.errorLevel().isEmpty()).isTrue();
            assertThat(validator.errorMessage().isEmpty()).isTrue();
        }
    }

    @Test
    void validators_are_by_default_triggered_when_the_property_changes() {
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property).withValidator(new StringLengthValidator("error", 1, 10));
        property.set("hello");
        {
            assertThat(validator.hasError().value()).isFalse();
            assertThat(validator.result().value()).containsExactly(ValidationResult.ok());
            assertThat(validator.errorLevel().isEmpty()).isTrue();
            assertThat(validator.errorMessage().isEmpty()).isTrue();
        }
        property.set("");
        {
            assertThat(validator.hasError().value()).isTrue();
            assertThat(validator.result().value()).containsExactly(ValidationResult.error("error"));
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.ERROR);
            assertThat(validator.errorMessage().value()).isEqualTo("error");
        }
    }

    @Test
    void validators_can_be_configured_to_use_explicit_validation_only() {
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property)
                .withValidator(new StringLengthValidator("error", 1, 10))
                .withValidationStrategy(PropertyValidator.ValidateOnCommand.INSTANCE);
        property.set("hello");
        {
            assertThat(validator.hasError().value()).isFalse();
            assertThat(validator.result().value()).isEmpty();
            assertThat(validator.errorLevel().isEmpty()).isTrue();
            assertThat(validator.errorMessage().isEmpty()).isTrue();
        }
        property.set("");
        {
            assertThat(validator.hasError().value()).isFalse();
            assertThat(validator.result().value()).isEmpty();
            assertThat(validator.errorLevel().isEmpty()).isTrue();
            assertThat(validator.errorMessage().isEmpty()).isTrue();
        }
        validator.validate();
        {
            assertThat(validator.hasError().value()).isTrue();
            assertThat(validator.result().value()).containsExactly(ValidationResult.error("error"));
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.ERROR);
            assertThat(validator.errorMessage().value()).isEqualTo("error");
        }
    }

    @Test
    void validation_results_can_be_cleared() {
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property).withValidator(new StringLengthValidator("error", 1, 10));
        validator.validate();
        {
            assertThat(validator.hasError().value()).isTrue();
            assertThat(validator.result().value()).containsExactly(ValidationResult.error("error"));
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.ERROR);
            assertThat(validator.errorMessage().value()).isEqualTo("error");
        }
        validator.clear();
        {
            assertThat(validator.hasError().value()).isFalse();
            assertThat(validator.result().value()).isEmpty();
            assertThat(validator.errorLevel().isEmpty()).isTrue();
            assertThat(validator.errorMessage().isEmpty()).isTrue();
        }
    }

    @Test
    void validators_can_be_grouped() {
        var verySlowValidators = new ValidatorGroup() {};
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property)
                .withValidator(new StringLengthValidator("length error", 1, 10))
                .withValidator(new EmailValidator("email error", true), verySlowValidators)
                .withValidationStrategy(PropertyValidator.ValidateOnCommand.INSTANCE);

        property.set("invalid email that is also too long");
        validator.validateGroups(PropertyValidator.DEFAULT_VALIDATOR_GROUP);
        {
            assertThat(validator.hasError().value()).isTrue();
            assertThat(validator.result().value()).containsOnly(ValidationResult.error("length error"));
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.ERROR);
            assertThat(validator.errorMessage().value()).isEqualTo("length error");
        }

        validator.validateGroups(verySlowValidators);
        {
            assertThat(validator.hasError().value()).isTrue();
            assertThat(validator.result().value()).containsOnly(ValidationResult.error("email error"));
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.ERROR);
            assertThat(validator.errorMessage().value()).isEqualTo("email error");
        }
    }

    @Test
    void default_validator_group_is_always_first() {
        var verySlowValidators = new ValidatorGroup() {};
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property)
                .withValidator(new StringLengthValidator("length error", 1, 10))
                .withValidator(new EmailValidator("email error", true), verySlowValidators);
        property.set("invalid email that is also too long");
        {
            assertThat(validator.result().value()).containsExactly(
                    ValidationResult.error("length error"),
                    ValidationResult.error("email error")
            );
            assertThat(validator.errorMessage().value()).isEqualTo("length error\nemail error");
        }
    }

    @Test
    void validators_can_be_disabled_and_enabled_by_group() {
        var verySlowValidators = new ValidatorGroup() {};
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property)
                .withValidator(new StringLengthValidator("length error", 1, 10))
                .withValidator(new EmailValidator("email error", true), verySlowValidators)
                .disableValidatorGroup(verySlowValidators);
        property.set("invalid email that is also too long");
        {
            assertThat(validator.result().value()).containsExactly(ValidationResult.error("length error"));
        }
        validator.enableValidatorGroup(verySlowValidators);
        {
            assertThat(validator.result().value()).containsExactly(
                    ValidationResult.error("length error"),
                    ValidationResult.error("email error")
            );
            assertThat(validator.errorMessage().value()).isEqualTo("length error\nemail error");
        }
    }

    @Test
    void validators_can_be_disabled_and_enabled_by_class() {
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property)
                .withValidator(new StringLengthValidator("length error", 1, 10))
                .withValidator(new EmailValidator("email error", true))
                .disableValidatorsOfType(EmailValidator.class);
        property.set("invalid email that is also too long");
        {
            assertThat(validator.result().value()).containsExactly(ValidationResult.error("length error"));
        }
        validator.enableValidatorsOfType(EmailValidator.class);
        {
            assertThat(validator.result().value()).containsExactly(
                    ValidationResult.error("length error"),
                    ValidationResult.error("email error")
            );
            assertThat(validator.errorMessage().value()).isEqualTo("length error\nemail error");
        }
    }

    @Test
    void most_critical_error_level_is_returned() {
        var errors = new ValidatorGroup() {};
        var warnings = new ValidatorGroup() {};
        var infos = new ValidatorGroup() {};
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property)
                .withValidator((Validator<String>) (value, context) -> ValidationResult.create("error", ErrorLevel.ERROR), errors)
                .withValidator((Validator<String>) (value, context) -> ValidationResult.create("warning", ErrorLevel.WARNING), warnings)
                .withValidator((Validator<String>) (value, context) -> ValidationResult.create("info", ErrorLevel.INFO), infos);
        property.set("does not matter what this is, the validation will fail anyway");
        {
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.ERROR);
        }
        validator.disableValidatorGroup(errors);
        {
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.WARNING);
        }
        validator.disableValidatorGroup(warnings);
        {
            assertThat(validator.errorLevel().value()).isEqualTo(ErrorLevel.INFO);
        }
    }
}
