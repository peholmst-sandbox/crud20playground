package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

final class DefaultRecordPropertySource<RECORD extends Record> extends AbstractRecordProperties<RECORD> implements RecordPropertySource<RECORD> {

    private final WritableProperty<Boolean> dirty = WritableProperty.create(false);

    DefaultRecordPropertySource(@Nonnull Class<RECORD> recordType) {
        super(recordType);
    }

    @Override
    protected void handlePropertyChangeEventWhenNotReading(@Nonnull PropertyValueChangeEvent<?> event) {
        dirty.set(true);
    }

    @Nonnull
    @Override
    public RECORD write() {
        return createRecord();
    }

    @Nonnull
    @Override
    public Property<Boolean> dirty() {
        return dirty;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(@Nonnull RECORD source) {
        readRecord(source);
        dirty.set(false);
    }
}
