package org.vaadin.playground.crud20.data.property.source;

import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableFunction;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractBeanProperties<BEAN> implements BeanProperties<BEAN> {

    /*
        The problem with using only getters and setters is that you can't use them as keys in a map.
        That means that if you call forProperty two times with the exact same methods, you will end up with
        two property entries. You could fix this with a metadata generator, or just make sure the users know
        about this quirk.
     */
    @SuppressWarnings("rawtypes")
    private final List<PropertyEntry> propertyEntries = new ArrayList<>();
    private final Constructor<BEAN> constructor;
    private boolean isReading = false;

    public AbstractBeanProperties(@Nonnull Class<BEAN> beanType) {
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void updateBean(@Nonnull BEAN bean) {
        propertyEntries.forEach(entry -> {
            if (entry instanceof PropertyEntry.WritablePropertyEntry writableEntry) {
                // Potential pitfall: if the bean setter wants a primitive and the Property contains null. By looking at
                // the setter reference alone there is no way of discovering whether the parameter is a boxed type
                // or a primitive type. The workaround is to have the user provide a custom non-null empty value for the property.
                writableEntry.setter().accept(bean, writableEntry.property().value());
            }
        });
    }

    @Nonnull
    @Override
    public <T> Property<T> forProperty(@Nonnull SerializableFunction<BEAN, T> getter) {
        return forPropertyWithEmptyValue(getter, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nonnull
    @Override
    public <T> Property<T> forPropertyWithEmptyValue(@Nonnull SerializableFunction<BEAN, T> getter, @Nullable T emptyValue) {
        var entry = new PropertyEntry.ReadOnlyPropertyEntry(createProperty(null), getter);
        propertyEntries.add(entry);
        return entry.property;
    }

    @Nonnull
    @Override
    public <T> WritableProperty<T> forProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter) {
        return forPropertyWithEmptyValue(getter, setter, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nonnull
    @Override
    public <T> WritableProperty<T> forPropertyWithEmptyValue(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter, @Nullable T emptyValue) {
        var entry = new PropertyEntry.WritablePropertyEntry(createProperty(emptyValue), getter, setter);
        propertyEntries.add(entry);
        return entry.property;
    }

    @SuppressWarnings("rawtypes")
    private WritableProperty createProperty(Object emptyValue) {
        var property = WritableProperty.createWithEmptyValue(emptyValue);
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
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull SerializableFunction<BEAN, T> getter) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull SerializableFunction<BEAN, T> getter, @Nonnull SerializableBiConsumer<BEAN, T> setter) {
        throw new UnsupportedOperationException("not implemented");
    }

    @SuppressWarnings("unchecked")
    protected void readBean(@Nullable BEAN source) {
        isReading = true;
        try {
            if (source == null) {
                propertyEntries.forEach(entry -> entry.property().clear());
            } else {
                propertyEntries.forEach(entry -> entry.property().set(entry.getter().apply(source)));
            }
        } finally {
            isReading = false;
        }
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
}
