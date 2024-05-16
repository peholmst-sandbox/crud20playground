package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import org.checkerframework.checker.units.qual.N;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class DefaultRecordPropertySource<RECORD extends Record> implements RecordPropertySource<RECORD> {

    private final Class<RECORD> recordType;
    private final List<RecordPropertyDefinition<Record, Object>> propertyDefinitions;
    @SuppressWarnings("rawtypes")
    private final Map<RecordPropertyDefinition, WritableProperty> propertyMap;
    private final WritableProperty<Boolean> dirty = WritableProperty.create(false);
    private final Constructor<RECORD> constructor;

    DefaultRecordPropertySource(@Nonnull Class<RECORD> recordType) {
        this.recordType = recordType;
        this.propertyDefinitions = Arrays.stream(recordType.getRecordComponents()).map(RecordPropertyDefinition::new).toList();
        this.propertyMap = propertyDefinitions.stream().collect(Collectors.toMap(def -> def, def -> createProperty()));
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
        property.addListener(event -> dirty.set(true));
        return property;
    }

    @Nonnull
    @Override
    public RECORD write() {
        var constructorArguments = propertyDefinitions.stream().map(prop -> prop.unwrapIfNecessary(propertyMap.get(prop).value())).toArray();
        try {
            return constructor.newInstance(constructorArguments);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create record", ex);
        }
    }

    @Nonnull
    @Override
    public Property<Boolean> dirty() {
        return dirty;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(@Nonnull RECORD source) {
        propertyMap.forEach((definition, property) -> property.set(definition.readValueFrom(source)));
        dirty.set(false);
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

    @Nonnull
    @Override
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty) {
        return null;
    }

    @Nonnull
    @Override
    public <T extends Record> RecordProperties<T> forRecordProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        return forRecordProperty(new RecordPropertyDefinition<>(recordType, propertyName, propertyType));
    }

    @Nonnull
    @Override
    public <T> BeanProperties<T> forBeanProperty(@Nonnull RecordPropertyDefinition<RECORD, T> recordProperty) {
        return null;
    }

    @Override
    public @N <T> BeanProperties<T> forBeanProperty(@Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        return forBeanProperty(new RecordPropertyDefinition<>(recordType, propertyName, propertyType));
    }
}
