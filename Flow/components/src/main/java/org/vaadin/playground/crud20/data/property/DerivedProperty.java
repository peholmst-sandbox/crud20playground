package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static java.util.Objects.requireNonNull;

final class DerivedProperty<T, S> extends AbstractProperty<T> {

    private final SerializableFunction<S, T> mapper;
    @SuppressWarnings("FieldCanBeLocal") // If used as a local field, the weak listener will be GC:d too early
    private final SerializableConsumer<PropertyValueChangeEvent<S>> onSourceValueChangeEvent = (event) -> updateCachedValue(event.value(), event.isEmpty());
    private T cachedValue;

    public DerivedProperty(@Nonnull AbstractProperty<S> source, @Nonnull SerializableFunction<S, T> mapper) {
        this.mapper = requireNonNull(mapper);
        source.addWeakListener(onSourceValueChangeEvent);
        updateCachedValue(source.value(), source.isEmpty());
    }

    private void updateCachedValue(@Nullable S sourceValue, boolean isEmpty) {
        var old = cachedValue;
        if (isEmpty) {
            cachedValue = null;
            log.trace("Updating cached value to null");
        } else {
            cachedValue = mapper.apply(sourceValue);
            log.trace("Updating cached value to [{}]", cachedValue);
        }
        notifyListeners(new PropertyValueChangeEvent<>(this, old, cachedValue));
    }

    @Nullable
    @Override
    public T value() {
        return cachedValue;
    }
}
