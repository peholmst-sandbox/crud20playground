package org.vaadin.playground.crud20.data.property.conversion;

import com.vaadin.flow.data.converter.StringToIntegerConverter;
import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;
import org.vaadin.playground.crud20.data.property.validation.ValidationState;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class PropertyConverterTest {

    @Test
    void can_be_converted_back_and_forth() {
        var property = WritableProperty.create(123);
        var converted = PropertyConverter.of(property, new StringToIntegerConverter("error"));
        assertThat(converted.value()).isEqualTo("123");
        assertThat(converted.validationState().value().isSuccess()).isTrue();
        converted.set("456");
        assertThat(property.value()).isEqualTo(456);
        assertThat(converted.validationState().value().isSuccess()).isTrue();
    }

    @Test
    void can_handle_conversion_errors() {
        var property = WritableProperty.create(123);
        var converted = PropertyConverter.of(property, new StringToIntegerConverter("error"));
        converted.set("not a number");
        assertThat(converted.value()).isEqualTo("not a number");
        assertThat(converted.validationState().value().isError()).isTrue();
        assertThat(converted.validationState().value())
                .asInstanceOf(type(ValidationState.Failure.class))
                .extracting(ValidationState.Failure::errorMessage)
                .isEqualTo("error");
        assertThat(property.value()).isEqualTo(123);
    }

    @Test
    void validation_state_can_be_listened_to() {
        var property = WritableProperty.create(123);
        var converted = PropertyConverter.of(property, new StringToIntegerConverter("error"));
        var event = new AtomicReference<PropertyValueChangeEvent<ValidationState>>();
        converted.validationState().addListener(event::set);
        converted.set("not a number");
        assertThat(event).hasValueSatisfying(e -> {
            assertThat(e.oldValue().isError()).isFalse();
            assertThat(e.value().isError()).isTrue();
        });
    }
}
