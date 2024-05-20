package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.requireNonNull;

abstract class AbstractProperty<T> implements Property<T> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final WeakHashMap<SerializableConsumer<PropertyValueChangeEvent<T>>, Void> weakListeners = new WeakHashMap<>();
    private final Set<SerializableConsumer<PropertyValueChangeEvent<T>>> listeners = new HashSet<>();

    @Override
    public abstract T value();

    @Override
    public T valueOrDefault(@Nullable T defaultValue) {
        return isPresent() ? value() : defaultValue;
    }

    @Override
    public boolean contains(@Nullable T value) {
        return Objects.equals(value, value());
    }

    @Override
    public boolean isEmpty() {
        return value() == null;
    }

    @Override
    public boolean isPresent() {
        return value() != null;
    }

    @Override
    @Nonnull
    public <E> Property<E> map(@Nonnull SerializableFunction<T, E> mapper) {
        return new DerivedProperty<>(this, mapper);
    }

    @Override
    @Nonnull
    public <E> Property<E> mapOptional(@Nonnull SerializableFunction<T, Optional<E>> mapper) {
        return new DerivedProperty<>(this, value -> mapper.apply(value).orElse(null));
    }

    @Override
    @Nonnull
    public Property<T> filter(@Nonnull SerializablePredicate<T> predicate) {
        return new DerivedProperty<>(this, value -> predicate.test(value) ? value : null);
    }

    @Override
    @Nonnull
    public Registration addListener(@Nonnull SerializableConsumer<PropertyValueChangeEvent<T>> listener) {
        log.trace("Adding listener {} with strong reference", listener);
        listeners.add(requireNonNull(listener));
        return () -> listeners.remove(listener);
    }

    public void addWeakListener(@Nonnull SerializableConsumer<PropertyValueChangeEvent<T>> listener) {
        log.trace("Adding listener {} with weak reference", listener);
        weakListeners.put(requireNonNull(listener), null);
    }

    protected void notifyListeners(@Nonnull PropertyValueChangeEvent<T> event) {
        requireNonNull(event);
        if (!listeners.isEmpty()) {
            Set.copyOf(listeners).forEach(listener -> notifyListener(event, listener));
        }
        if (!weakListeners.isEmpty()) {
            Set.copyOf(weakListeners.keySet()).forEach(listener -> notifyListener(event, listener));
        }
    }

    private void notifyListener(@Nonnull PropertyValueChangeEvent<T> event, @Nonnull SerializableConsumer<PropertyValueChangeEvent<T>> listener) {
        log.trace("Notifying listener {} of event {}", listener, event);
        try {
            listener.accept(event);
        } catch (Exception ex) {
            log.error("Error in listener", ex);
        }
    }
}
