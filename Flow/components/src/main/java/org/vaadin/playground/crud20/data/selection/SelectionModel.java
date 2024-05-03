package org.vaadin.playground.crud20.data.selection;

import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract sealed class SelectionModel<T> implements Serializable permits SingleSelectionModel, MultiSelectionModel {

    private final List<SelectionChangeListener<T>> selectionChangeListenerList = new LinkedList<>();
    private Set<T> selection = Collections.emptySet();

    public @Nonnull Registration addSelectionChangeListener(@Nonnull SelectionChangeListener<T> listener) {
        selectionChangeListenerList.add(listener);
        return () -> selectionChangeListenerList.remove(listener);
    }

    public @Nonnull Set<T> selection() {
        return selection;
    }

    protected void setSelection(@Nonnull Set<T> selection) {
        if (this.selection.equals(selection)) {
            return;
        }
        var old = this.selection;
        this.selection = selection;
        fireSelectionChangeEvent(new SelectionChangeEvent<>(this, old, selection));
    }

    public void clearSelection() {
        setSelection(Collections.emptySet());
    }

    protected void fireSelectionChangeEvent(@Nonnull SelectionChangeEvent<T> event) {
        List.copyOf(selectionChangeListenerList).forEach(listener -> listener.onSelectionChange(event));
    }

    public static <T> @Nonnull MultiSelectionModel<T> multi() {
        return new MultiSelectionModel<>();
    }

    public static <T> @Nonnull SingleSelectionModel<T> single() {
        return new SingleSelectionModel<>();
    }
}
