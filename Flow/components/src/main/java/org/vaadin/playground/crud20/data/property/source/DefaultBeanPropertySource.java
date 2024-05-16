package org.vaadin.playground.crud20.data.property.source;

import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableFunction;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.WritableProperty;

class DefaultBeanPropertySource<BEAN> implements BeanPropertySource<BEAN> {

    private final Class<BEAN> beanType;

    DefaultBeanPropertySource(@Nonnull Class<BEAN> beanType) {
        this.beanType = beanType;
    }

    @Nonnull
    @Override
    public BEAN write(@Nullable BEAN target) {
        return null;
    }

    @Nonnull
    @Override
    public <T> Property<T> forProperty(@Nonnull SerializableFunction<BEAN, T> getter) {
        return null;
    }

    @Nonnull
    @Override
    public <T> WritableProperty<T> forProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter) {
        return null;
    }

    @Nonnull
    @Override
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter) {
        return null;
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull SerializableFunction<BEAN, T> getter) {
        return null;
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter) {
        return null;
    }

    @Nonnull
    @Override
    public Property<Boolean> dirty() {
        return null;
    }

    @Override
    public void read(@Nonnull BEAN source) {

    }
}
