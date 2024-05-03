package org.vaadin.playground.crud20.data.selection;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

public interface SelectionChangeListener<T> extends Serializable {

    void onSelectionChange(@Nonnull SelectionChangeEvent<T> event);
}
