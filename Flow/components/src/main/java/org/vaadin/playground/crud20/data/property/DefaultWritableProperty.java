package org.vaadin.playground.crud20.data.property;

import jakarta.annotation.Nullable;

class DefaultWritableProperty<T> extends AbstractWritableProperty<T> implements WritableProperty<T> {

    private final T emptyValue;

    public DefaultWritableProperty() {
        this(null, null);
    }

    public DefaultWritableProperty(T initialValue, @Nullable T emptyValue) {
        super(initialValue);
        this.emptyValue = emptyValue;
    }

    @Nullable
    @Override
    public T emptyValue() {
        return emptyValue;
    }

    @Override
    public void set(T value) {
        doSet(value);
    }
}
