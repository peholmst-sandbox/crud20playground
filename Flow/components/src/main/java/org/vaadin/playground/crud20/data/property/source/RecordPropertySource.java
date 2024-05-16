package org.vaadin.playground.crud20.data.property.source;

import jakarta.annotation.Nonnull;

public interface RecordPropertySource<RECORD extends Record> extends PropertySource<RECORD>, RecordProperties<RECORD> {

    @Nonnull
    RECORD write();
}
