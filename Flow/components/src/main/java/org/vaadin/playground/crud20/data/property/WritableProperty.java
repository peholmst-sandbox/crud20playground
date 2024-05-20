package org.vaadin.playground.crud20.data.property;

import jakarta.annotation.Nonnull;

public interface WritableProperty<T> extends Property<T> {

    default void set(T value) {
        set(value, false);
    }

    void set(T value, boolean force);

    void clear();

    static <T> @Nonnull WritableProperty<T> create() {
        return new DefaultWritableProperty<>();
    }

    static <T> @Nonnull WritableProperty<T> create(T initialValue) {
        return new DefaultWritableProperty<>(initialValue);
    }
}
