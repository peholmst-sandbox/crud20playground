package org.vaadin.playground.crud20.data.property;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public record PropertyValueChangeEvent<T>(@Nonnull Property<T> source,
                                          T oldValue,
                                          T value)
        implements Serializable {

    public PropertyValueChangeEvent {
        requireNonNull(source);
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isPresent() {
        return value != null;
    }
}
