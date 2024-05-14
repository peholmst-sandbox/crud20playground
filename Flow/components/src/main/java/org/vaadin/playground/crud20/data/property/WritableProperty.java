package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.data.converter.Converter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface WritableProperty<T> extends Property<T> {

    @Nonnull
    <E> ConvertedProperty<E> convert(@Nonnull Converter<E, T> converter);

    default void set(T value) {
        set(value, false);
    }

    void set(T value, boolean force);

    void clear();

    static <T> @Nonnull WritableProperty<T> create() {
        return new DefaultWritableProperty<>();
    }

    static <T> @Nonnull WritableProperty<T> create(T initialValue) {
        return new DefaultWritableProperty<>(initialValue, null);
    }

    // TODO createWithEmptyValue needs a better name. Now you'd think it creates a new, empty property.

    static <T> @Nonnull WritableProperty<T> createWithEmptyValue(@Nullable T emptyValue) {
        return new DefaultWritableProperty<>(emptyValue, emptyValue);
    }

    static <T> @Nonnull WritableProperty<T> createWithEmptyValue(T initialValue, @Nullable T emptyValue) {
        return new DefaultWritableProperty<>(initialValue, emptyValue);
    }
}
