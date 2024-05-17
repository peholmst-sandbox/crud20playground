package org.vaadin.playground.crud20.data.property.source;

import com.vaadin.flow.function.SerializableConsumer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

abstract class AbstractRecordProperties<RECORD extends Record> implements RecordProperties<RECORD> {

    private final Class<RECORD> recordType;
    private final List<RecordPropertyDefinition<Record, Object>> propertyDefinitions;
    @SuppressWarnings("rawtypes")
    private final Map<RecordPropertyDefinition, WritableProperty> propertyMap;
    @SuppressWarnings("rawtypes")
    private final Map<RecordPropertyDefinition, PropertyBackedRecordProperties> recordPropertyMap;
    private final Constructor<RECORD> constructor;
    private boolean isReading = false;

    public AbstractRecordProperties(@Nonnull Class<RECORD> recordType) {
        this.recordType = recordType;
        this.propertyDefinitions = Arrays.stream(recordType.getRecordComponents()).map(RecordPropertyDefinition::new).toList();
        this.propertyMap = propertyDefinitions.stream().collect(Collectors.toMap(def -> def, def -> createProperty()));
        this.recordPropertyMap = new HashMap<>();
        try {
            this.constructor = recordType.getDeclaredConstructor(
                    Arrays.stream(recordType.getRecordComponents())
                            .map(RecordComponent::getType).toArray(Class[]::new));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No canonical constructor found", e);
        }
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

    protected @Nonnull RECORD createRecord() {
        var constructorArguments = propertyDefinitions.stream().map(prop -> prop.unwrapIfNecessary(propertyMap.get(prop).value())).toArray();
        try {
            return constructor.newInstance(constructorArguments);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create record", ex);
        }
    }

    protected Optional<RECORD> createRecordIfNotEmpty() {
        if (propertyMap.values().stream().allMatch(Property::isEmpty)) {
            return Optional.empty();
        } else {
            return Optional.of(createRecord());
        }
    }

    @SuppressWarnings("unchecked")
    protected void readRecord(@Nullable RECORD source) {
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

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> WritableProperty<T> forProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty) {
        return (WritableProperty<T>) propertyMap.get(recordProperty); // Should never be null because RecordPropertyDefinition checks for existence
    }

    @Nonnull
    @Override
    public <T> WritableProperty<T> forProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        return forProperty(new RecordPropertyDefinition<>(recordType, propertyName, propertyType));
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty) {
        return recordPropertyMap.computeIfAbsent(recordProperty, prop -> new PropertyBackedRecordProperties<>(recordProperty.propertyType(), forProperty(recordProperty)));
    }

    @Nonnull
    @Override
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        return forRecordProperty(new RecordPropertyDefinition<>(recordType, propertyName, propertyType));
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public @Nonnull <T> BeanProperties<T> forBeanProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        return forBeanProperty(new RecordPropertyDefinition<>(recordType, propertyName, propertyType));
    }

    static class PropertyBackedRecordProperties<RECORD extends Record> extends AbstractRecordProperties<RECORD> {

        private final WritableProperty<RECORD> sourceProperty;
        private boolean updatingSource = false;
        @SuppressWarnings("FieldCanBeLocal")
        private final SerializableConsumer<PropertyValueChangeEvent<RECORD>> onSourcePropertyChange = (event) -> {
            if (!updatingSource) {
                readRecord(event.value());
            }
        };

        public PropertyBackedRecordProperties(@Nonnull Class<RECORD> recordType, @Nonnull WritableProperty<RECORD> sourceProperty) {
            super(recordType);
            this.sourceProperty = requireNonNull(sourceProperty);
            this.sourceProperty.addWeakListener(onSourcePropertyChange);
        }

        @Override
        protected void handlePropertyChangeEventWhenNotReading(@Nonnull PropertyValueChangeEvent<?> event) {
            updatingSource = true;
            try {
                createRecordIfNotEmpty().ifPresentOrElse(sourceProperty::set, sourceProperty::clear);
            } finally {
                updatingSource = false;
            }
        }
    }
}
