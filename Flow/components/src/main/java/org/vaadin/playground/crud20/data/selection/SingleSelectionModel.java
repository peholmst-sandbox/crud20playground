package org.vaadin.playground.crud20.data.selection;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Set;

public final class SingleSelectionModel<T> extends SelectionModel<T> {

    public @Nonnull Registration addAction(@Nonnull SerializableConsumer<T> action,
                                           @Nonnull SerializableRunnable emptyAction) {
        selection().stream().findFirst().ifPresentOrElse(action, emptyAction);
        return addSelectionChangeListener(event -> event.firstSelectedItem().ifPresentOrElse(action, emptyAction));
    }

    public void select(@Nullable T selectedItem) {
        if (selectedItem == null) {
            clearSelection();
        } else {
            setSelection(Set.of(selectedItem));
        }
    }
}
