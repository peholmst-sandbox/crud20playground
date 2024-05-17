package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;

public final class ReadOnlyBeanPropertyDefinition<BEAN, T> extends BeanPropertyDefinition<BEAN, T> {

    public ReadOnlyBeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType, @Nonnull String getterName) {
        super(beanClass, propertyName, propertyType, getterName);
    }

    public ReadOnlyBeanPropertyDefinition(@Nonnull Class<BEAN> beanClass, @Nonnull String propertyName, @Nonnull Class<T> propertyType) {
        super(beanClass, propertyName, propertyType);
    }
}
