package org.vaadin.playground.crud20.data.property.source;

import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableFunction;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.io.Serializable;

public interface BeanProperties<BEAN> extends Serializable {

    @Nonnull
    <T> Property<T> forProperty(@Nonnull SerializableFunction<BEAN, T> getter);

    @Nonnull
    <T> Property<T> forPropertyWithEmptyValue(@Nonnull SerializableFunction<BEAN, T> getter, @Nullable T emptyValue);

    @Nonnull
    <T> WritableProperty<T> forProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter);

    @Nonnull
    <T> WritableProperty<T> forPropertyWithEmptyValue(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter, @Nullable T emptyValue);

    @Nonnull
    <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter);

    @Nonnull
    <T> BeanProperties<T> forBeanProperty(@Nonnull SerializableFunction<BEAN, T> getter);

    @Nonnull
    <T> BeanProperties<T> forBeanProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter);
}
