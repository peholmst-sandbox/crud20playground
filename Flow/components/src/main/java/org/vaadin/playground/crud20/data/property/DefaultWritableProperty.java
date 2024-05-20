package org.vaadin.playground.crud20.data.property;

class DefaultWritableProperty<T> extends AbstractWritableProperty<T> implements WritableProperty<T> {

    public DefaultWritableProperty() {
        this(null);
    }

    public DefaultWritableProperty(T initialValue) {
        super(initialValue);
    }

    @Override
    public void set(T value, boolean force) {
        doSet(value, force);
    }
}
