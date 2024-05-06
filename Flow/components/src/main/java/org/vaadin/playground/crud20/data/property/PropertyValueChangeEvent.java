package org.vaadin.playground.crud20.data.property;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public record PropertyValueChangeEvent<T>(@Nonnull Property<T> source,
                                          @Nullable T oldValue,
                                          @Nullable T value)
        implements Serializable {

    public boolean isEmpty() {
        return Objects.equals(value, source.emptyValue());
    }

    public boolean isPresent() {
        return !isEmpty();
    }
}
