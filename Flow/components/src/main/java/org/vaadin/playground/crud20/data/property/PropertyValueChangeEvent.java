package org.vaadin.playground.crud20.data.property;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public record PropertyValueChangeEvent<T>(@Nonnull Property<T> source,
                                          T oldValue,
                                          T value)
        implements Serializable {

    public PropertyValueChangeEvent {
        requireNonNull(source);
    }

    public boolean isEmpty() {
        return Objects.equals(value, source.emptyValue());
    }

    public boolean isPresent() {
        return !isEmpty();
    }
}
