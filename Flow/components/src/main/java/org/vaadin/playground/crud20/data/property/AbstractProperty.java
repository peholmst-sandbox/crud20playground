package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.function.SerializableRunnable;
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
    @Nullable
    public abstract T emptyValue();

    @Override
    public boolean contains(@Nullable T value) {
        return Objects.equals(value, value());
    }

    @Override
    public boolean isEmpty() {
        return contains(emptyValue());
    }

    @Override
    public boolean isPresent() {
        return !isEmpty();
    }

    @Override
    @Nonnull
    public <E> Property<E> map(@Nonnull SerializableFunction<T, E> mapper) {
        return map(mapper, null);
    }

    @Override
    @Nonnull
    public <E> Property<E> map(@Nonnull SerializableFunction<T, E> mapper, @Nullable E emptyValue) {
        return new DerivedProperty<>(this, mapper, emptyValue);
    }

    @Override
    @Nonnull
    public <E> Property<E> mapOptional(@Nonnull SerializableFunction<T, Optional<E>> mapper) {
        return mapOptional(mapper, null);
    }

    @Override
    @Nonnull
    public <E> Property<E> mapOptional(@Nonnull SerializableFunction<T, Optional<E>> mapper, @Nullable E emptyValue) {
        return new DerivedProperty<>(this, value -> mapper.apply(value).orElse(emptyValue), emptyValue);
    }

    @Override
    @Nonnull
    public Property<T> filter(@Nonnull SerializablePredicate<T> predicate) {
        return new DerivedProperty<>(this, value -> predicate.test(value) ? value : emptyValue(), emptyValue());
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

    @Override
    public void doIfPresent(@Nonnull SerializableConsumer<T> action) {
        if (isPresent()) {
            log.trace("Invoking action {}", action);
            action.accept(value());
        }
    }

    @Override
    public void doIfPresentOrElse(@Nonnull SerializableConsumer<T> action, @Nonnull SerializableRunnable emptyAction) {
        if (isPresent()) {
            log.trace("Invoking action {}", action);
            action.accept(value());
        } else {
            log.trace("Invoking empty action {}", emptyAction);
            emptyAction.run();
        }
    }

    @Override
    @Nonnull
    public Registration triggerIfPresent(@Nonnull SerializableConsumer<T> action) {
        return triggerIfPresent(action, true);
    }

    @Override
    @Nonnull
    public Registration triggerIfPresent(@Nonnull SerializableConsumer<T> action, boolean initialTrigger) {
        if (initialTrigger) {
            doIfPresent(action);
        }
        return addListener(event -> {
            if (event.isPresent()) {
                log.trace("Invoking action {} in response to event {}", action, event);
                action.accept(event.value());
            }
        });
    }

    @Override
    @Nonnull
    public Registration triggerIfPresentOrElse(@Nonnull SerializableConsumer<T> action, @Nonnull SerializableRunnable emptyAction) {
        return triggerIfPresentOrElse(action, emptyAction, true);
    }

    @Override
    @Nonnull
    public Registration triggerIfPresentOrElse(@Nonnull SerializableConsumer<T> action, @Nonnull SerializableRunnable emptyAction, boolean initialTrigger) {
        if (initialTrigger) {
            doIfPresentOrElse(action, emptyAction);
        }
        return addListener(event -> {
            if (event.isPresent()) {
                log.trace("Invoking action {} in response to event {}", action, event);
                action.accept(event.value());
            } else {
                log.trace("Invoking empty action {} in response to event {}", emptyAction, event);
                emptyAction.run();
            }
        });
    }
}
