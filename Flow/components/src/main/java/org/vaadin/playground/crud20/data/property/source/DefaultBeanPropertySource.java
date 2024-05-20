package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

final class DefaultBeanPropertySource<BEAN> extends AbstractBeanProperties<BEAN> implements BeanPropertySource<BEAN> {

    private final WritableProperty<Boolean> dirty = WritableProperty.create(false);

    DefaultBeanPropertySource(@Nonnull Class<BEAN> beanType) {
        super(beanType);
    }

    @Override
    protected void handlePropertyChangeEventWhenNotReading(@Nonnull PropertyValueChangeEvent<?> event) {
        dirty.set(true);
    }

    @Nonnull
    @Override
    public BEAN write(@Nullable BEAN target) {
        if (target == null) {
            return createBean();
        } else {
            updateBean(target);
            return target;
        }
    }

    @Nonnull
    @Override
    public Property<Boolean> dirty() {
        return dirty;
    }

    @Override
    public void read(@Nonnull BEAN source) {
        readBean(source);
        dirty.set(false);
    }
}
