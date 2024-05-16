package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import org.checkerframework.checker.units.qual.N;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.io.Serializable;

public interface RecordProperties<RECORD extends Record> extends Serializable {

    @Nonnull
    <T> WritableProperty<T> forProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty);

    @Nonnull
    <T> WritableProperty<T> forProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType);

    @Nonnull
    <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty);

    @Nonnull
    <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType);

    @Nonnull
    <T> BeanProperties<T> forBeanProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty);

    @N
    <T> BeanProperties<T> forBeanProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType);
}
