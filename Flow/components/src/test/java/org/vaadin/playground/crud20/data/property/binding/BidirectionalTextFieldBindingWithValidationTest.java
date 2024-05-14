package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.property.WritableProperty;
import org.vaadin.playground.crud20.data.property.validation.PropertyValidator;

import static org.assertj.core.api.Assertions.assertThat;

public class BidirectionalTextFieldBindingWithValidationTest {

    @Test
    void binding_alone_does_not_trigger_validation() {
        var property = WritableProperty.create("h");
        var validator = PropertyValidator.of(property).withValidator(new StringLengthValidator("error", 2, 10));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(property, textField);
        PropertyBinding.bindValidationState(validator, textField);
        {
            assertThat(textField.getValue()).isEqualTo("h");
            assertThat(textField.isInvalid()).isFalse();
            assertThat(validator.validationState().value().isUnknown()).isTrue();
        }
    }

    @Test
    void text_field_validation_state_is_updated_when_property_validator_is_updated() {
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property).withValidator(new StringLengthValidator("error", 2, 10));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(property, textField);
        PropertyBinding.bindValidationState(validator, textField);
        textField.setValue("h");
        {
            assertThat(textField.getValue()).isEqualTo("h");
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("error");
        }
        textField.setValue("world");
        {
            assertThat(textField.getValue()).isEqualTo("world");
            assertThat(textField.isInvalid()).isFalse();
        }
        textField.setValue("12345678910");
        {
            assertThat(textField.getValue()).isEqualTo("12345678910");
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("error");
        }
    }

    @Test
    void binding_can_be_removed() {
        var property = WritableProperty.create("");
        var validator = PropertyValidator.of(property).withValidator(new StringLengthValidator("error", 2, 10));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(property, textField);
        var binding = PropertyBinding.bindValidationState(validator, textField);
        binding.remove();
        validator.validate();
        {
            assertThat(textField.isInvalid()).isFalse();
            assertThat(textField.getErrorMessage()).isNullOrEmpty();
        }
    }
}
