package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.Optional;

public interface Property<T> extends Serializable {

    T value();

    T valueOrDefault(@Nullable T defaultValue);

    boolean contains(@Nullable T value);

    boolean isEmpty();

    boolean isPresent();

    @Nonnull
    <E> Property<E> map(@Nonnull SerializableFunction<T, E> mapper);

    @Nonnull
    <E> Property<E> mapOptional(@Nonnull SerializableFunction<T, Optional<E>> mapper);

    @Nonnull
    Property<T> filter(@Nonnull SerializablePredicate<T> predicate);

    @Nonnull
    Registration addListener(@Nonnull SerializableConsumer<PropertyValueChangeEvent<T>> listener);

    void addWeakListener(@Nonnull SerializableConsumer<PropertyValueChangeEvent<T>> listener);
}
