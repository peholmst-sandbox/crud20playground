package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

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

    @SuppressWarnings("unchecked")
    @Nullable
    T readValueFrom(@Nonnull BEAN bean) {
        try {
            return (T) getter().invoke(bean);
        } catch (Exception e) {
            throw new IllegalStateException("Could not read from bean", e);
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanPropertyDefinition<?, ?> that = (BeanPropertyDefinition<?, ?>) o;
        return Objects.equals(beanClass, that.beanClass) && Objects.equals(propertyName, that.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanClass, propertyName);
    }
}
