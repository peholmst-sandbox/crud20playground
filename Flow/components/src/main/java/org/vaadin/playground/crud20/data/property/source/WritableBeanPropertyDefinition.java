package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;

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
