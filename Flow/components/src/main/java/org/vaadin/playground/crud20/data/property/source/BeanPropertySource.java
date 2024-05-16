package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface BeanPropertySource<BEAN> extends PropertySource<BEAN>, BeanProperties<BEAN> {

    default @Nonnull BEAN write() {
        return write(null);
    }

    @Nonnull
    BEAN write(@Nullable BEAN target);
}
