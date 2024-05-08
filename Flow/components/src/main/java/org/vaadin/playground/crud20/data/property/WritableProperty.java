package org.vaadin.playground.crud20.data.property;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class WritableProperty<T> extends Property<T> {

    private final T emptyValue;
    private T value;

    public WritableProperty() {
        this(null, null);
    }

    public WritableProperty(T initialValue, @Nullable T emptyValue) {
        this.value = initialValue;
        this.emptyValue = emptyValue;
    }

    @Nullable
    @Override
    public final T emptyValue() {
        return emptyValue;
    }

    public final void set(T value) {
        if (Objects.equals(this.value, value)) {
            return;
        }
        var old = this.value;
        this.value = value;
        notifyListeners(new PropertyValueChangeEvent<>(this, old, this.value));
    }

    public final void clear() {
        set(emptyValue);
    }

    @Override
    public final T value() {
        return value;
    }

    public static <T> @Nonnull WritableProperty<T> create() {
        return new WritableProperty<>();
    }

    public static <T> @Nonnull WritableProperty<T> create(T initialValue) {
        return new WritableProperty<>(initialValue, null);
    }
}
