package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.*;

import static java.util.Objects.requireNonNull;

public abstract class Property<T> implements Serializable {

    private final WeakHashMap<SerializableConsumer<PropertyValueChangeEvent<T>>, Void> weakListeners = new WeakHashMap<>();
    private final Set<SerializableConsumer<PropertyValueChangeEvent<T>>> listeners = new HashSet<>();

    @Nullable
    public abstract T value();

    @Nullable
    public abstract T emptyValue();

    public final boolean contains(@Nullable T value) {
        return Objects.equals(value(), value);
    }

    public final boolean isEmpty() {
        return contains(emptyValue());
    }

    public final boolean isPresent() {
        return !isEmpty();
    }

    @Nonnull
    public final <E> Property<E> map(@Nonnull SerializableFunction<T, E> mapper) {
        return map(mapper, null);
    }

    @Nonnull
    public final <E> Property<E> map(@Nonnull SerializableFunction<T, E> mapper, @Nullable E emptyValue) {
        return new DerivedProperty<>(this, mapper, emptyValue);
    }

    @Nonnull
    public final <E> Property<E> mapOptional(@Nonnull SerializableFunction<T, Optional<E>> mapper) {
        return mapOptional(mapper, null);
    }

    @Nonnull
    public final <E> Property<E> mapOptional(@Nonnull SerializableFunction<T, Optional<E>> mapper, @Nullable E emptyValue) {
        return new DerivedProperty<>(this, value -> mapper.apply(value).orElse(emptyValue), emptyValue);
    }

    @Nonnull
    public final Property<T> filter(@Nonnull SerializablePredicate<T> predicate) {
        return new DerivedProperty<>(this, value -> predicate.test(value) ? value : emptyValue(), emptyValue());
    }

    @Nonnull
    public final Registration addListener(@Nonnull SerializableConsumer<PropertyValueChangeEvent<T>> listener) {
        listeners.add(requireNonNull(listener));
        return () -> listeners.remove(listener);
    }

    protected final void addWeakListener(@Nonnull SerializableConsumer<PropertyValueChangeEvent<T>> listener) {
        weakListeners.put(requireNonNull(listener), null);
    }

    protected final void notifyListeners(@Nonnull PropertyValueChangeEvent<T> event) {
        requireNonNull(event);
        Set.copyOf(listeners).forEach(listener -> listener.accept(event));
        Set.copyOf(weakListeners.keySet()).forEach(listener -> listener.accept(event));
    }

    public final void doIfPresent(@Nonnull SerializableConsumer<T> action) {
        if (isPresent()) {
            action.accept(value());
        }
    }

    public final void doIfPresentOrElse(@Nonnull SerializableConsumer<T> action, @Nonnull SerializableRunnable emptyAction) {
        if (isPresent()) {
            action.accept(value());
        } else {
            emptyAction.run();
        }
    }

    @Nonnull
    public final Registration triggerIfPresent(@Nonnull SerializableConsumer<T> action) {
        return triggerIfPresent(action, true);
    }

    @Nonnull
    public final Registration triggerIfPresent(@Nonnull SerializableConsumer<T> action, boolean initialTrigger) {
        if (initialTrigger) {
            doIfPresent(action);
        }
        return addListener(event -> {
            if (event.isPresent()) {
                action.accept(event.value());
            }
        });
    }

    @Nonnull
    public final Registration triggerIfPresentOrElse(@Nonnull SerializableConsumer<T> action, @Nonnull SerializableRunnable emptyAction) {
        return triggerIfPresentOrElse(action, emptyAction, true);
    }

    @Nonnull
    public final Registration triggerIfPresentOrElse(@Nonnull SerializableConsumer<T> action, @Nonnull SerializableRunnable emptyAction, boolean initialTrigger) {
        if (initialTrigger) {
            doIfPresentOrElse(action, emptyAction);
        }
        return addListener(event -> {
            if (event.isPresent()) {
                action.accept(event.value());
            } else {
                emptyAction.run();
            }
        });
    }
}
