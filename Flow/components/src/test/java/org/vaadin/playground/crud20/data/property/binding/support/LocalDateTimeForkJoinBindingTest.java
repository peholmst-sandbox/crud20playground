package org.vaadin.playground.crud20.data.property.binding.support;

import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeForkJoinBindingTest {

    @Test
    void joins_are_incomplete_if_not_all_the_necessary_data_are_present() {
        var property = WritableProperty.<LocalDateTime>create();
        var binding = new LocalDateTimeForkJoinBinding(property);
        {
            assertThat(binding.state().value().isIncomplete()).isTrue();
        }
        binding.dateProperty().set(LocalDate.now());
        {
            assertThat(binding.state().value().isIncomplete()).isTrue();
        }
        binding.dateProperty().clear();
        binding.timeProperty().set(LocalTime.now());
        {
            assertThat(binding.state().value().isIncomplete()).isTrue();
        }
    }

    @Test
    void joins_are_complete_when_all_necessary_data_are_present() {
        var property = WritableProperty.<LocalDateTime>create();
        var binding = new LocalDateTimeForkJoinBinding(property);
        binding.dateProperty().set(LocalDate.of(2024, 5, 14));
        binding.timeProperty().set(LocalTime.of(10, 8, 31));
        {
            assertThat(binding.state().value().isComplete()).isTrue();
            assertThat(property.value()).isEqualTo(LocalDateTime.of(2024, 5, 14, 10, 8, 31));
        }
    }

    @Test
    void fork_properties_are_initialized_from_the_join_property() {
        var property = WritableProperty.create(LocalDateTime.of(2024, 5, 14, 10, 8, 31));
        var binding = new LocalDateTimeForkJoinBinding(property);
        {
            assertThat(binding.dateProperty().value()).isEqualTo(LocalDate.of(2024, 5, 14));
            assertThat(binding.timeProperty().value()).isEqualTo(LocalTime.of(10, 8, 31));
            assertThat(binding.state().value().isComplete()).isTrue();
        }
    }

    @Test
    void fork_properties_are_changed_when_the_join_property_changes() {
        var property = WritableProperty.<LocalDateTime>create();
        var binding = new LocalDateTimeForkJoinBinding(property);
        property.set(LocalDateTime.of(2024, 5, 14, 10, 8, 31));
        {
            assertThat(binding.dateProperty().value()).isEqualTo(LocalDate.of(2024, 5, 14));
            assertThat(binding.timeProperty().value()).isEqualTo(LocalTime.of(10, 8, 31));
            assertThat(binding.state().value().isComplete()).isTrue();
        }
    }

    @Test
    void fork_properties_are_cleared_when_the_join_property_is_cleared() {
        var property = WritableProperty.create(LocalDateTime.of(2024, 5, 14, 10, 8, 31));
        var binding = new LocalDateTimeForkJoinBinding(property);
        property.clear();
        {
            assertThat(binding.dateProperty().isEmpty()).isTrue();
            assertThat(binding.timeProperty().isEmpty()).isTrue();
            assertThat(binding.state().value().isComplete()).isTrue();
        }
    }

    @Test
    void join_property_is_cleared_when_all_fork_properties_are_cleared() {
        var value = LocalDateTime.of(2024, 5, 14, 10, 8, 31);
        var property = WritableProperty.create(value);
        var binding = new LocalDateTimeForkJoinBinding(property);
        binding.dateProperty().clear();
        {
            assertThat(property.value()).isEqualTo(value);
            assertThat(binding.state().value().isIncomplete()).isTrue();
        }
        binding.timeProperty().clear();
        {
            assertThat(property.isEmpty()).isTrue();
            assertThat(binding.state().value().isComplete()).isTrue();
        }
    }
}
