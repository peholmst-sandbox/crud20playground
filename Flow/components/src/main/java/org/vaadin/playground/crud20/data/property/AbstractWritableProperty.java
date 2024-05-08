package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.data.converter.Converter;
import jakarta.annotation.Nonnull;

import java.util.Objects;

abstract class AbstractWritableProperty<T> extends AbstractProperty<T> implements WritableProperty<T> {

    private T value;

    public AbstractWritableProperty() {
    }

    public AbstractWritableProperty(T initialValue) {
        this.value = initialValue;
    }

    @Override
    @Nonnull
    public <E> ConvertedProperty<E> convert(@Nonnull Converter<E, T> converter) {
        return new DefaultConvertedProperty<>(this, converter);
    }

    protected void doSet(T value) {
        if (Objects.equals(this.value, value)) {
            return;
        }
        var old = this.value;
        this.value = value;
        notifyListeners(new PropertyValueChangeEvent<>(this, old, this.value));
    }

    @Override
    public void clear() {
        set(emptyValue());
    }

    @Override
    public T value() {
        return value;
    }
}
