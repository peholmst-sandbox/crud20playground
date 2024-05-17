package org.vaadin.playground.crud20.data.property.source;

import com.vaadin.flow.function.SerializableConsumer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

abstract class AbstractBeanProperties<BEAN> implements BeanProperties<BEAN> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final Class<BEAN> beanType;
    @SuppressWarnings("rawtypes")
    private final Map<BeanPropertyDefinition, WritableProperty> propertyMap = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private final Map<BeanPropertyDefinition, PropertyBackedBeanProperties> beanPropertyMap = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private final Map<BeanPropertyDefinition, AbstractRecordProperties.PropertyBackedRecordProperties> recordPropertyMap = new HashMap<>();
    private final Constructor<BEAN> constructor;
    private boolean isReading = false;

    public AbstractBeanProperties(@Nonnull Class<BEAN> beanType) {
        // TODO Consider introspecting the beanType class and generating properties for all getters and setters.
        //  That way, the behavior would be the same as for records.
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

    protected boolean isEmpty() {
        return propertyMap.values().stream().noneMatch(Property::isPresent);
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

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty) {
        return recordPropertyMap.computeIfAbsent(beanProperty, prop -> new AbstractRecordProperties.PropertyBackedRecordProperties<>(beanProperty.propertyType, forProperty(beanProperty)));
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull ReadOnlyBeanPropertyDefinition<BEAN, T> beanProperty) {
        return beanPropertyMap.computeIfAbsent(beanProperty, prop -> new PropertyBackedBeanProperties<>(beanProperty.propertyType(), forProperty(beanProperty)));
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull WritableBeanPropertyDefinition<BEAN, T> beanProperty) {
        return beanPropertyMap.computeIfAbsent(beanProperty, prop -> new PropertyBackedBeanProperties<>(beanProperty.propertyType, forProperty(beanProperty)));
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
        log.trace("Updating bean {}", bean);
        propertyMap.forEach((definition, property) -> {
            if (definition instanceof WritableBeanPropertyDefinition writableBeanPropertyDefinition) {
                log.trace("Writing value to bean: {} = [{}]", definition, property.value());
                writableBeanPropertyDefinition.writeValueTo(bean, property.value());
            } else {
                var readOnlyBean = definition.readValueFrom(bean);
                if (readOnlyBean == null) {
                    log.warn("Bean property {} is null, cannot update in any way, skipping", definition);
                } else {
                    var readOnlyProperties = beanPropertyMap.get(definition);
                    if (readOnlyProperties != null) {
                        log.trace("Delegating to bean properties in order to update {}", definition);
                        readOnlyProperties.updateBean(readOnlyBean);
                    } else {
                        log.warn("Unable to update bean property {} because no bean properties are available", definition);
                    }
                }
            }
        });
    }

    static class PropertyBackedBeanProperties<BEAN> extends AbstractBeanProperties<BEAN> {

        private final Property<BEAN> sourceProperty;
        private boolean updatingSource = false;
        @SuppressWarnings("FieldCanBeLocal")
        private final SerializableConsumer<PropertyValueChangeEvent<BEAN>> onSourcePropertyChange = (event) -> {
            if (!updatingSource) {
                readBean(event.value());
            }
        };

        public PropertyBackedBeanProperties(@Nonnull Class<BEAN> beanType, @Nonnull Property<BEAN> sourceProperty) {
            super(beanType);
            this.sourceProperty = requireNonNull(sourceProperty);
            this.sourceProperty.addWeakListener(onSourcePropertyChange);
        }

        @Override
        protected void handlePropertyChangeEventWhenNotReading(@Nonnull PropertyValueChangeEvent<?> event) {
            updatingSource = true;
            try {
                if (sourceProperty instanceof WritableProperty<BEAN> writableProperty) {
                    var isEmpty = isEmpty();
                    if (isEmpty) {
                        writableProperty.clear();
                    } else if (writableProperty.isPresent()) {
                        updateBean(writableProperty.value());
                    } else {
                        writableProperty.set(createBean());
                    }
                } else if (sourceProperty.isPresent()) {
                    updateBean(sourceProperty.value());
                }
            } finally {
                updatingSource = false;
            }
        }
    }
}
