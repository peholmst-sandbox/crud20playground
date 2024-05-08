package org.vaadin.playground.crud20.data.property;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class WritablePropertyTest {

    @Test
    void can_be_empty_initially() {
        var property = WritableProperty.<String>create();
        assertThat(property.isEmpty()).isTrue();
        assertThat(property.value()).isEqualTo(null);
    }

    @Test
    void can_have_initial_value() {
        var property = WritableProperty.create("initial value");
        assertThat(property.isPresent()).isTrue();
        assertThat(property.value()).isEqualTo("initial value");
    }

    @Test
    void listeners_are_called_when_value_is_changed() {
        var property = WritableProperty.create("initial value");
        var event = new AtomicReference<PropertyValueChangeEvent<String>>();
        property.addListener(event::set);
        property.set("new value");
        assertThat(event).hasValueSatisfying(e -> {
            assertThat(e.source()).isSameAs(property);
            assertThat(e.oldValue()).isEqualTo("initial value");
            assertThat(e.value()).isEqualTo("new value");
        });
    }

    @Test
    void can_be_mapped_to_other_type() {
        var property = WritableProperty.create("123");
        var mapped = property.map(Integer::parseInt);
        assertThat(mapped.value()).isEqualTo(123);
    }

    @Test
    void mapped_types_can_be_listened_to() {
        var property = WritableProperty.create("123");
        var mapped = property.map(Integer::parseInt);
        var event = new AtomicReference<PropertyValueChangeEvent<Integer>>();
        mapped.addListener(event::set);
        property.set("456");
        assertThat(event).hasValueSatisfying(e -> {
            assertThat(e.source()).isSameAs(mapped);
            assertThat(e.oldValue()).isEqualTo(123);
            assertThat(e.value()).isEqualTo(456);
        });
    }

    @Test
    void can_be_filtered() {
        var property = WritableProperty.create("123");
        var filtered = property.filter(s -> s.length() > 2);
        assertThat(filtered.value()).isEqualTo("123");
        property.set("12");
        assertThat(filtered.isEmpty()).isTrue();
        property.set("1234");
        assertThat(filtered.value()).isEqualTo("1234");
    }

    @Test
    void can_have_custom_empty_value() {
        var property = new WritableProperty<>("initial value", "empty");
        assertThat(property.isPresent()).isTrue();
        property.clear();
        assertThat(property.isEmpty()).isTrue();
        assertThat(property.value()).isEqualTo("empty");
    }

    @Test
    void custom_empty_values_work_with_filtering() {
        var property = new WritableProperty<>("123", "empty");
        var filtered = property.filter(s -> s.length() > 2);
        assertThat(filtered.value()).isEqualTo("123");
        property.set("12");
        assertThat(filtered.isEmpty()).isTrue();
        assertThat(filtered.value()).isEqualTo("empty");
    }

    @Test
    void custom_empty_values_work_with_mapping() {
        var property = new WritableProperty<>("123", "");
        var mapped = property.map(Integer::parseInt, -1);
        assertThat(mapped.value()).isEqualTo(123);
        property.clear();
        assertThat(mapped.value()).isEqualTo(-1);
    }
}
