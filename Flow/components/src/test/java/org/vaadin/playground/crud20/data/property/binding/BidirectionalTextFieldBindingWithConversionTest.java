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
    void conversion_and_validation_can_be_used_together() {
        var property = WritableProperty.<Integer>create();
        var validator = PropertyValidator.of(property).withValidator(new IntegerRangeValidator("validation error", 0, 100));
        var convertedProperty = property.convert(new StringToIntegerConverter("conversion error"));
        var textField = new TextField();
        PropertyBinding.bindValueBidirectionally(convertedProperty, textField);
        PropertyBinding.bindValidationState(List.of(convertedProperty, validator), textField);

        System.out.println(">>> Setting to string value");
        textField.setValue("this is not an integer");
        {
//            assertThat(convertedProperty.conversionState().isError()).isTrue();
//            assertThat(convertedProperty.value()).isEqualTo("this is not an integer");
//            assertThat(property.isEmpty()).isTrue();
//            assertThat(validator.hasError().value()).isFalse();
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("conversion error");
        }
        System.out.println(">>> Setting to -1");
        textField.setValue("-1");
        {
//            assertThat(convertedProperty.conversionState().isError()).isFalse();
//            assertThat(convertedProperty.value()).isEqualTo("-1");
//            assertThat(property.value()).isEqualTo(-1);
//            assertThat(validator.hasError().value()).isTrue();
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("validation error");
        }
        System.out.println(">>> Setting to 101");
        textField.setValue("101");
        {
//            assertThat(convertedProperty.conversionState().isError()).isFalse();
//            assertThat(convertedProperty.value()).isEqualTo("101");
//            assertThat(property.value()).isEqualTo(101);
//            assertThat(validator.hasError().value()).isTrue();
            assertThat(textField.isInvalid()).isTrue();
            assertThat(textField.getErrorMessage()).isEqualTo("validation error");
        }
        System.out.println(">>> Setting to 12");
        textField.setValue("12");
        {
//            assertThat(convertedProperty.conversionState().isError()).isFalse();
//            assertThat(convertedProperty.value()).isEqualTo("12");
//            assertThat(property.value()).isEqualTo(12);
//            assertThat(validator.hasError().value()).isFalse();
            assertThat(textField.isInvalid()).isFalse();
            assertThat(textField.getErrorMessage()).isNullOrEmpty();
        }
    }
}
