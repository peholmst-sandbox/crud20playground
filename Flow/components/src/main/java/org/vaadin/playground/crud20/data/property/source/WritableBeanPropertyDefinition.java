package org.vaadin.playground.crud20.data.property.source;

import com.google.common.base.Defaults;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;

public final class WritableBeanPropertyDefinition<BEAN, T> extends BeanPropertyDefinition<BEAN, T> {

    private final String setterName;
    private transient Method setter;

    public WritableBeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType, @Nonnull String getterName, @Nonnull String setterName) {
        super(beanClass, propertyName, propertyType, getterName);
        this.setterName = requireNonNull(setterName);
        setter();
    }

    public WritableBeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        super(beanClass, propertyName, propertyType);
        this.setterName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        setter();
    }

    void writeValueTo(@Nonnull BEAN bean, @Nullable T value) {
        try {
            setter().invoke(bean, unwrapIfNecessary(value));
        } catch (Exception e) {
            throw new IllegalStateException("Could not write to bean", e);
        }
    }

    @Nullable
    T unwrapIfNecessary(@Nullable T value) {
        if (propertyType.isPrimitive() && value == null) {
            return Defaults.defaultValue(propertyType); // This is using Google Guava. Could be replaced by an inline statement.
        }
        return value;
    }

    private @Nonnull Method setter() {
        if (setter != null) {
            return setter;
        }
        try {
            return setter = beanClass.getDeclaredMethod(setterName, propertyType);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Setter method not found: " + setterName, e);
        }
    }
}
