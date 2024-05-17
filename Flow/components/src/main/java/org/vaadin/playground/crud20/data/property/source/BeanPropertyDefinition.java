package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;

public sealed abstract class BeanPropertyDefinition<BEAN, T> implements Serializable permits ReadOnlyBeanPropertyDefinition, WritableBeanPropertyDefinition {

    protected final Class<BEAN> beanClass;
    private final String propertyName;
    private final String getterName;
    protected final Class<T> propertyType;
    private transient Method getter;

    public BeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType, @Nonnull String getterName) {
        this.beanClass = requireNonNull(beanClass);
        this.propertyName = requireNonNull(propertyName);
        this.getterName = requireNonNull(getterName);
        this.propertyType = requireNonNull(propertyType);

        getter();
    }

    public BeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        this.beanClass = requireNonNull(beanClass);
        this.propertyName = requireNonNull(propertyName);
        this.propertyType = requireNonNull(propertyType);
        this.getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    protected final @Nonnull Method getter() {
        if (getter != null) {
            return getter;
        }
        try {
            return getter = beanClass.getDeclaredMethod(getterName); // TODO Check that return type matches propertyType
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Getter method not found: " + getterName, e);
        }
    }
}
