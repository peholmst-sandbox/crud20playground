package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;

import java.io.Serializable;

public interface PropertySource<T> extends Serializable {

    @Nonnull
    Property<Boolean> dirty();

    void read(@Nonnull T source);

    @Nonnull
    static <RECORD extends Record> RecordPropertySource<RECORD> forRecord(@Nonnull Class<RECORD> recordType) {
        return new DefaultRecordPropertySource<>(recordType);
    }

    @Nonnull
    static <BEAN> BeanPropertySource<BEAN> forBean(@Nonnull Class<BEAN> beanType) {
        return new DefaultBeanPropertySource<>(beanType);
    }
}
