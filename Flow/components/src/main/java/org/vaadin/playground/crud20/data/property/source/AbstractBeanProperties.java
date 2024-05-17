package org.vaadin.playground.crud20.data.property.source;

import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

abstract class AbstractBeanProperties<BEAN> implements BeanProperties<BEAN> {

    private final Class<BEAN> beanType;
    @SuppressWarnings("rawtypes")
    private final Map<BeanPropertyDefinition, WritableProperty> propertyMap = new HashMap<>();
    private final Constructor<BEAN> constructor;
    private boolean isReading = false;

    public AbstractBeanProperties(@Nonnull Class<BEAN> beanType) {
        this.beanType = requireNonNull(beanType);
        try {
            constructor = beanType.getConstructor();
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("No default constructor found", ex);
        }
    }

    protected @Nonnull BEAN createBean() {
        try {
            var bean = constructor.newInstance();
            updateBean(bean);
            return bean;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create bean", ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> Property<T> forProperty(@Nonnull ReadOnlyBeanPropertyDefinition<BEAN, T> beanProperty) {
        return propertyMap.computeIfAbsent(beanProperty, prop -> createProperty());
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> WritableProperty<T> forProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty) {
        return propertyMap.computeIfAbsent(beanProperty, prop -> createProperty());
    }

    @SuppressWarnings("rawtypes")
    private WritableProperty createProperty() {
        var property = WritableProperty.create();
        property.addListener(this::handlePropertyChangeEvent);
        return property;
    }

    protected void handlePropertyChangeEvent(@Nonnull PropertyValueChangeEvent<?> event) {
        if (!isReading) {
            handlePropertyChangeEventWhenNotReading(event);
        }
    }

    protected void handlePropertyChangeEventWhenNotReading(@Nonnull PropertyValueChangeEvent<?> event) {
        // NOP, should be overridden
    }

    @Nonnull
    @Override
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty) {
        return null;
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull ReadOnlyBeanPropertyDefinition<BEAN, T> beanProperty) {
        return null;
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty) {
        return null;
    }

    @Nonnull
    @Override
    public <T> ReadOnlyBeanPropertyDefinition<BEAN, T> getReadOnlyPropertyDefinition(@Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        return new ReadOnlyBeanPropertyDefinition<>(beanType, propertyName, propertyType);
    }

    @Nonnull
    @Override
    public <T> WritableBeanPropertyDefinition<BEAN, T> getWritablePropertyDefinition(@Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        return new WritableBeanPropertyDefinition<>(beanType, propertyName, propertyType);
    }

    @SuppressWarnings("unchecked")
    protected void readBean(@Nullable BEAN source) {
        isReading = true;
        try {
            if (source == null) {
                propertyMap.values().forEach(WritableProperty::clear);
            } else {
                propertyMap.forEach((definition, property) -> property.set(definition.readValueFrom(source)));
            }
        } finally {
            isReading = false;
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void updateBean(@Nonnull BEAN bean) {
        propertyMap.forEach((definition, property) -> {
            if (definition instanceof WritableBeanPropertyDefinition writableBeanPropertyDefinition) {
                writableBeanPropertyDefinition.writeValueTo(bean, property.value());
            }
        });
    }

    sealed interface PropertyEntry<BEAN, T> {
        record WritablePropertyEntry<BEAN, T>(
                WritableProperty<T> property,
                SerializableFunction<BEAN, T> getter,
                SerializableBiConsumer<BEAN, T> setter
        ) implements PropertyEntry<BEAN, T> {
        }

        record ReadOnlyPropertyEntry<BEAN, T>(
                WritableProperty<T> property,
                SerializableFunction<BEAN, T> getter
        ) implements PropertyEntry<BEAN, T> {
        }

        WritableProperty<T> property();

        SerializableFunction<BEAN, T> getter();
    }

    static class PropertyBackedBeanProperties<BEAN> extends AbstractBeanProperties<BEAN> {

        private final Property<BEAN> sourceProperty;
        private final SerializableConsumer<PropertyValueChangeEvent<BEAN>> onSourcePropertyChange = (event) -> {

        };

        public PropertyBackedBeanProperties(@Nonnull Class<BEAN> beanType, @Nonnull Property<BEAN> sourceProperty) {
            super(beanType);
            this.sourceProperty = requireNonNull(sourceProperty);
            this.sourceProperty.addWeakListener(onSourcePropertyChange);
        }

    }
}
