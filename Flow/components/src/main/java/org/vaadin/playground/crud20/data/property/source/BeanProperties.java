package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.io.Serializable;

public interface BeanProperties<BEAN> extends Serializable {

    @Nonnull
    <T> Property<T> forProperty(@Nonnull ReadOnlyBeanPropertyDefinition<BEAN, T> beanProperty);

    @Nonnull
    <T> WritableProperty<T> forProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty);

    @Nonnull
    <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty);

    @Nonnull
    <T> BeanProperties<T> forBeanProperty(@Nonnull ReadOnlyBeanPropertyDefinition<BEAN, T> beanProperty);

    @Nonnull
    <T> BeanProperties<T> forBeanProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty);

    @Nonnull
    <T> WritableBeanPropertyDefinition<BEAN, T> getWritablePropertyDefinition(@Nonnull String propertyName, @Nonnull Class<T> propertyType);

    @Nonnull
    <T> ReadOnlyBeanPropertyDefinition<BEAN, T> getReadOnlyPropertyDefinition(@Nonnull String propertyName, @Nonnull Class<T> propertyType);
}
