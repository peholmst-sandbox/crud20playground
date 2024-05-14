package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.property.WritableProperty;
import org.vaadin.playground.crud20.data.property.validation.PropertyValidator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BidirectionalTextFieldBindingWithConversionTest {

    @Test
    void initial_successful_state_is_immediately_updated_when_bound() {
        var property = WritableProperty.<Integer>create();
        var convertedProperty = property.convert(new StringToIntegerConverter("conversion error"));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(convertedProperty, textField);
        PropertyBinding.bindValidationState(convertedProperty, textField);
        {
            assertThat(textField.isInvalid()).isFalse();
            assertThat(textField.getErrorMessage()).isNullOrEmpty();
        }
    }

    @Test
    void initial_failure_state_is_immediately_updated_when_bound() {
        var property = WritableProperty.<Integer>create();
        var convertedProperty = property.convert(new StringToIntegerConverter("conversion error"));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(convertedProperty, textField);
        textField.setValue("this is not an integer");
        PropertyBinding.bindValidationState(convertedProperty, textField);
        {
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("conversion error");
        }
    }

    @Test
    void text_field_validation_state_is_updated_when_conversion_state_is_updated() {
        var property = WritableProperty.<Integer>create();
        var convertedProperty = property.convert(new StringToIntegerConverter("conversion error"));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(convertedProperty, textField);
        PropertyBinding.bindValidationState(convertedProperty, textField);
        textField.setValue("this is not an integer");
        {
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("conversion error");
        }
        textField.setValue("123");
        {
            assertThat(textField.isInvalid()).isFalse();
            assertThat(textField.getErrorMessage()).isNullOrEmpty();
            assertThat(property.value()).isEqualTo(123);
        }
    }

    @Test
    void binding_can_be_removed() {
        var property = WritableProperty.<Integer>create();
        var convertedProperty = property.convert(new StringToIntegerConverter("conversion error"));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(convertedProperty, textField);
        var binding = PropertyBinding.bindValidationState(convertedProperty, textField);
        binding.remove();
        textField.setValue("this is not an integer");
        {
            assertThat(textField.isInvalid()).isFalse();
            assertThat(textField.getErrorMessage()).isNullOrEmpty();
            assertThat(convertedProperty.validationState().value().isError()).isTrue();
        }
    }

    @Test
    void binding_can_be_disabled_and_enabled() {
        var property = WritableProperty.<Integer>create();
        var convertedProperty = property.convert(new StringToIntegerConverter("conversion error"));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(convertedProperty, textField);
        var binding = PropertyBinding.bindValidationState(convertedProperty, textField);
        binding.disable();
        textField.setValue("this is not an integer");
        {
            assertThat(textField.isInvalid()).isFalse();
            assertThat(textField.getErrorMessage()).isNullOrEmpty();
            assertThat(convertedProperty.validationState().value().isError()).isTrue();
        }
        binding.enable();
        {
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("conversion error");
        }
    }

    @Test
    void conversion_and_validation_can_be_used_together() {
        var property = WritableProperty.<Integer>create();
        var validator = PropertyValidator.of(property).withValidator(new IntegerRangeValidator("validation error", 0, 100));
        var convertedProperty = property.convert(new StringToIntegerConverter("conversion error"));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(convertedProperty, textField);
        PropertyBinding.bindValidationState(List.of(convertedProperty, validator), textField);

        textField.setValue("this is not an integer");
        {
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("conversion error");
        }
        textField.setValue("-1");
        {
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("validation error");
        }
        textField.setValue("101");
        {
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("validation error");
        }
        textField.setValue("12");
        {
            assertThat(textField.isInvalid()).isFalse();
            assertThat(textField.getErrorMessage()).isNullOrEmpty();
        }
    }
}
