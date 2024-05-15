package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import static org.assertj.core.api.Assertions.assertThat;

public class UnidirectionalTextFieldBindingTest {

    @Test
    void text_field_is_updated_with_property_initial_value() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValue(property, textField);
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
    }

    @Test
    void text_field_is_cleared_if_property_is_initially_empty() {
        var textField = new TextField();
        textField.setValue("this will be removed");
        var property = WritableProperty.<String>create();
        PropertyBinding.bindValue(property, textField);
        {
            assertThat(textField.getValue()).isEmpty();
        }
    }

    @Test
    void text_field_is_updated_when_property_is_updated() {
        var textField = new TextField();
        var property = WritableProperty.<String>create();
        PropertyBinding.bindValue(property, textField);
        property.set("hello");
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
    }

    @Test
    void text_field_is_cleared_when_property_is_cleared() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValue(property, textField);
        property.clear();
        {
            assertThat(textField.getValue()).isEmpty();
        }
    }

    @Test
    void property_is_not_updated_when_text_field_is_updated() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValue(property, textField);
        textField.setValue("world");
        {
            assertThat(property.value()).isEqualTo("hello");
        }
    }

    @Test
    void multiple_fields_can_be_bound_to_the_same_property() {
        var textField1 = new TextField();
        var textField2 = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValue(property, textField1);
        PropertyBinding.bindValue(property, textField2);
        {
            assertThat(textField1.getValue()).isEqualTo("hello");
            assertThat(textField2.getValue()).isEqualTo("hello");
        }
        property.set("world");
        {
            assertThat(textField1.getValue()).isEqualTo("world");
            assertThat(textField2.getValue()).isEqualTo("world");
        }
    }

    @Test
    void bindings_can_be_removed_and_re_added() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        var binding = PropertyBinding.bindValue(property, textField);
        binding.remove();
        property.set("world");
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
        binding.bind();
        {
            assertThat(textField.getValue()).isEqualTo("world");
        }
    }

    @Test
    void bindings_can_be_removed_on_detach() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValue(property, textField).removeOnDetach(textField);
        ComponentUtil.onComponentDetach(textField);
        property.set("world");
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
    }

    @Test
    void bindings_can_be_re_added_on_attach() {
        var textField = new TextField();
        var property = WritableProperty.create("hello");
        PropertyBinding.bindValue(property, textField).removeOnDetach(textField).rebindOnAttach(textField);
        ComponentUtil.onComponentAttach(textField, true);
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
        ComponentUtil.onComponentDetach(textField);
        property.set("world");
        {
            assertThat(textField.getValue()).isEqualTo("hello");
        }
        ComponentUtil.onComponentAttach(textField, false);
        {
            assertThat(textField.getValue()).isEqualTo("world");
        }
    }
}
