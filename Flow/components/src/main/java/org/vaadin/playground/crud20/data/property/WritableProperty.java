package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.data.converter.Converter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface WritableProperty<T> extends Property<T> {

    @Nonnull
    <E> ConvertedProperty<E> convert(@Nonnull Converter<E, T> converter);

    void set(T value);

    void clear();

    static <T> @Nonnull WritableProperty<T> create() {
        return new DefaultWritableProperty<>();
    }

    static <T> @Nonnull WritableProperty<T> create(T initialValue) {
        return new DefaultWritableProperty<>(initialValue, null);
    }

    static <T> @Nonnull WritableProperty<T> createWithEmptyValue(@Nullable T emptyValue) {
        return new DefaultWritableProperty<>(null, emptyValue);
    }

    static <T> @Nonnull WritableProperty<T> createWithEmptyValue(T initialValue, @Nullable T emptyValue) {
        return new DefaultWritableProperty<>(initialValue, emptyValue);
    }
}
