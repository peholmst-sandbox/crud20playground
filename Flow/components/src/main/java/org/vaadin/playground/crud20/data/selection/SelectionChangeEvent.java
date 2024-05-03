package org.vaadin.playground.crud20.data.selection;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public final class SelectionChangeEvent<T> implements Serializable {

    private final SelectionModel<T> source;
    private final Set<T> oldSelection;
    private final Set<T> selection;

    SelectionChangeEvent(@Nonnull SelectionModel<T> source, @Nonnull Set<T> oldSelection, @Nonnull Set<T> selection) {
        this.source = source;
        this.oldSelection = oldSelection;
        this.selection = selection;
    }

    public @Nonnull SelectionModel<T> source() {
        return source;
    }

    public @Nonnull Set<T> oldSelection() {
        return oldSelection;
    }

    public @Nonnull Optional<T> firstOldSelectedItem() {
        return oldSelection.stream().findFirst();
    }

    public @Nonnull Set<T> selection() {
        return selection;
    }

    public @Nonnull Optional<T> firstSelectedItem() {
        return selection.stream().findFirst();
    }
}
