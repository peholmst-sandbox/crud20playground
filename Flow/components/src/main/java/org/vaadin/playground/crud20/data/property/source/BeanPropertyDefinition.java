package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;

public final class BeanPropertyDefinition<BEAN, T> implements Serializable {

    private final Class<BEAN> beanClass;
    private final String propertyName;
    private final String getterName;
    private final String setterName;
    private final Class<T> propertyType;
    private transient Method getter;
    private transient Method setter;

    public BeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType, @Nonnull String getterName, @Nullable String setterName) {
        this.beanClass = requireNonNull(beanClass);
        this.propertyName = requireNonNull(propertyName);
        this.getterName = requireNonNull(getterName);
        this.propertyType = requireNonNull(propertyType);
        this.setterName = requireNonNull(setterName);

        setter();
        getter();
    }

    public BeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType, boolean readOnly) {
        this.beanClass = requireNonNull(beanClass);
        this.propertyName = requireNonNull(propertyName);
        this.propertyType = requireNonNull(propertyType);
        this.getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        this.setterName = readOnly ? null : "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    private @Nonnull Method getter() {
        if (getter != null) {
            return getter;
        }
        try {
            return getter = beanClass.getDeclaredMethod(getterName); // TODO Check that return type matches propertyType
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Getter method not found: " + getterName, e);
        }
    }

    private @Nullable Method setter() {
        if (setter != null) {
            return setter;
        }
        if (setterName == null) {
            return null;
        }
        try {
            return setter = beanClass.getDeclaredMethod(setterName, propertyType);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Setter method not found: " + setterName, e);
        }
    }
}
