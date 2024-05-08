package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public final class DerivedProperty<T, S> extends Property<T> {

    private final SerializableFunction<S, T> mapper;
    private final T emptyValue;
    @SuppressWarnings("FieldCanBeLocal") // If used as a local field, the weak listener will be GC:d too early
    private final SerializableConsumer<PropertyValueChangeEvent<S>> onSourceValueChangeEvent = (event) -> updateCachedValue(event.value(), event.isEmpty());
    private T cachedValue;

    public DerivedProperty(@Nonnull Property<S> source, @Nonnull SerializableFunction<S, T> mapper, @Nullable T emptyValue) {
        this.mapper = requireNonNull(mapper);
        this.emptyValue = emptyValue;
        this.cachedValue = emptyValue;
        source.addWeakListener(onSourceValueChangeEvent);
        updateCachedValue(source.value(), source.isEmpty());
    }

    private void updateCachedValue(@Nullable S sourceValue, boolean isEmpty) {
        var old = cachedValue;
        if (isEmpty) {
            cachedValue = emptyValue();
        } else {
            cachedValue = mapper.apply(sourceValue);

        }
        notifyListeners(new PropertyValueChangeEvent<>(this, old, cachedValue));
    }

    @Nullable
    @Override
    public T emptyValue() {
        return emptyValue;
    }

    @Nullable
    @Override
    public T value() {
        return cachedValue;
    }
}
