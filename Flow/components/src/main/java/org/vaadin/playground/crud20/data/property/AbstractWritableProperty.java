package org.vaadin.playground.crud20.data.property;

import java.util.Objects;

public abstract class AbstractWritableProperty<T> extends AbstractProperty<T> implements WritableProperty<T> {

    private T value;

    public AbstractWritableProperty() {
    }

    public AbstractWritableProperty(T initialValue) {
        this.value = initialValue;
    }

    protected boolean doSet(T value, boolean force) {
        if (!force && Objects.equals(this.value, value)) {
            log.trace("Ignoring setting value because current value [{}] is same as new value", value);
            return false;
        }
        log.trace("Setting new value to [{}] (force = {})", value, force);
        var old = this.value;
        this.value = value;
        notifyListeners(new PropertyValueChangeEvent<>(this, old, this.value));
        return true;
    }

    @Override
    public void clear() {
        set(null);
    }

    @Override
    public T value() {
        return value;
    }
}
