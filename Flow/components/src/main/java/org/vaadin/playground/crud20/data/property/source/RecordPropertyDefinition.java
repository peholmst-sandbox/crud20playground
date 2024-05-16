package org.vaadin.playground.crud20.data.property.source;

import com.google.common.base.Defaults;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class RecordPropertyDefinition<RECORD extends Record, T> implements Serializable {

    private final Class<RECORD> recordClass;
    private final String propertyName;
    private final Class<T> propertyType;
    private transient RecordComponent recordComponent;

    public RecordPropertyDefinition(@Nonnull Class<RECORD> recordClass,
                                    @Nonnull String propertyName,
                                    @Nonnull Class<T> propertyType) {
        this.recordClass = requireNonNull(recordClass);
        this.propertyName = requireNonNull(propertyName);
        this.propertyType = requireNonNull(propertyType);

        if (!wrapIfNecessary(recordComponent().getType()).isAssignableFrom(propertyType)) {
            throw new IllegalArgumentException("Property type mismatch: " + recordComponent().getType() + " != " + propertyType);
        }
    }

    private static @Nonnull Class<?> wrapIfNecessary(@Nonnull Class<?> type) {
        return MethodType.methodType(type).wrap().returnType();
    }

    @SuppressWarnings("unchecked")
    RecordPropertyDefinition(@Nonnull RecordComponent recordComponent) {
        this.recordClass = (Class<RECORD>) recordComponent.getDeclaringRecord();
        this.propertyName = recordComponent.getName();
        this.propertyType = (Class<T>) recordComponent.getType();
        this.recordComponent = recordComponent;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    T readValueFrom(@Nonnull RECORD record) {
        try {
            return (T) recordComponent().getAccessor().invoke(record);
        } catch (Exception e) {
            throw new IllegalStateException("Could not read from record", e);
        }
    }

    @Nullable
    T unwrapIfNecessary(@Nullable T value) {
        if (propertyType.isPrimitive() && value == null) {
            return Defaults.defaultValue(propertyType); // This is using Google Guava. Could be replaced by an inline statement.
        }
        return value;
    }

    private @Nonnull RecordComponent recordComponent() {
        if (recordComponent != null) {
            return recordComponent;
        }
        return recordComponent = Arrays.stream(recordClass.getRecordComponents())
                .filter(component -> component.getName().equals(propertyName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such property: " + propertyName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordPropertyDefinition<?, ?> that = (RecordPropertyDefinition<?, ?>) o;
        return Objects.equals(recordClass, that.recordClass)
                && Objects.equals(propertyName, that.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordClass, propertyName);
    }
}
