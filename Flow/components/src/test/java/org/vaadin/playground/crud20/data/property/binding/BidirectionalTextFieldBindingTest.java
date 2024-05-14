package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import static org.assertj.core.api.Assertions.assertThat;

public class BidirectionalTextFieldBindingTest {

    @Test
    void text_field_is_updated_with_property_initial_value() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValueBidirectionally(property, textField);
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
    }

    @Test
    void text_field_is_cleared_if_property_is_initially_empty() {
        var textField = new TextField();
        textField.setValue("this will be removed");
        var property = WritableProperty.<String>create();
        PropertyBinding.bindValueBidirectionally(property, textField);
        {
            assertThat(textField.getValue()).isEmpty();
        }
    }

    @Test
    void text_field_is_updated_when_property_is_updated() {
        var textField = new TextField();
        var property = WritableProperty.<String>create();
        PropertyBinding.bindValueBidirectionally(property, textField);
        property.set("hello");
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
    }

    @Test
    void text_field_is_cleared_when_property_is_cleared() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValueBidirectionally(property, textField);
        property.clear();
        {
            assertThat(textField.getValue()).isEmpty();
        }
    }

    @Test
    void property_is_updated_when_text_field_is_updated() {
        var textField = new TextField();
        var property = WritableProperty.<String>create();
        PropertyBinding.bindValueBidirectionally(property, textField);
        textField.setValue("hello");
        {
            assertThat(property.value()).isEqualTo("hello");
        }
    }

    @Test
    void property_is_cleared_when_text_field_is_cleared() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValueBidirectionally(property, textField);
        textField.clear();
        {
            assertThat(property.isEmpty()).isTrue();
        }
    }

    @Test
    void multiple_fields_can_be_bound_to_the_same_property() {
        var textField1 = new TextField();
        var textField2 = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValueBidirectionally(property, textField1);
        PropertyBinding.bindValueBidirectionally(property, textField2);
        {
            assertThat(textField1.getValue()).isEqualTo("hello");
            assertThat(textField2.getValue()).isEqualTo("hello");
        }
        property.set("world");
        {
            assertThat(textField1.getValue()).isEqualTo("world");
            assertThat(textField2.getValue()).isEqualTo("world");
        }
        textField1.setValue("foo");
        {
            assertThat(property.value()).isEqualTo("foo");
            assertThat(textField2.getValue()).isEqualTo("foo");
        }
        textField2.setValue("bar");
        {
            assertThat(property.value()).isEqualTo("bar");
            assertThat(textField1.getValue()).isEqualTo("bar");
        }
    }

    @Test
    void bindings_can_be_disabled_and_enabled() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        var binding = PropertyBinding.bindValueBidirectionally(property, textField);
        binding.disable();
        property.set("world");
        textField.setValue("foo");
        {
            assertThat(textField.getValue()).isEqualTo("foo");
            assertThat(property.value()).isEqualTo("world");
        }
        binding.enable();
        {
            assertThat(textField.getValue()).isEqualTo("world");
            assertThat(property.value()).isEqualTo("world");
        }
    }

    @Test
    void bindings_can_be_removed() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        var binding = PropertyBinding.bindValueBidirectionally(property, textField);
        binding.remove();
        property.set("world");
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
        textField.setValue("foo");
        {
            assertThat(property.value()).isEqualTo("world");
        }
    }
}
