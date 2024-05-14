package org.vaadin.playground.crud20.data.property.binding.support;

import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.WritableProperty;
import org.vaadin.playground.crud20.data.property.binding.ForkJoinBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// This class is just included as an example. We could choose to provide built-in bindings for certain types,
// or just let the developers extend ForkJoinBinding when needed.
public class LocalDateTimeForkJoinBinding extends ForkJoinBinding<LocalDateTime> {

    private final WritableProperty<LocalTime> timeProperty = WritableProperty.create();
    private final WritableProperty<LocalDate> dateProperty = WritableProperty.create();

    public LocalDateTimeForkJoinBinding(@Nonnull WritableProperty<LocalDateTime> dateTimeProperty) {
        super(dateTimeProperty);
        registerForkProperty(timeProperty);
        registerForkProperty(dateProperty);
    }

    @Override
    protected void fork(@Nonnull Property<LocalDateTime> joinProperty) {
        if (joinProperty.isPresent()) {
            var value = joinProperty.value();
            dateProperty.set(value.toLocalDate());
            timeProperty.set(value.toLocalTime());
        } else {
            dateProperty.clear();
            timeProperty.clear();
        }
    }

    @Nonnull
    @Override
    protected JoinState attemptJoin(@Nonnull Property<LocalDateTime> joinProperty) {
        if (timeProperty.isEmpty() && dateProperty.isEmpty()) {
            return new JoinState.Complete<>(joinProperty.emptyValue());
        }
        var time = timeProperty.value();
        var date = dateProperty.value();
        if (time == null || date == null) {
            return new JoinState.Incomplete();
        }
        return new JoinState.Complete<>(LocalDateTime.of(date, time));
    }

    public @Nonnull WritableProperty<LocalTime> timeProperty() {
        return timeProperty;
    }

    public @Nonnull WritableProperty<LocalDate> dateProperty() {
        return dateProperty;
    }
}
