package org.vaadin.playground.crud20.demo.common.domain.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to be implemented by classes that have an ID (such as a database primary key).
 *
 * @param <ID> the type of the ID.
 */
public interface HasId<ID> {

    /**
     * Returns the object's ID, or {@code null} if the object does not have one.
     */
    @Nullable ID id();

    /**
     * Returns the object's ID.
     *
     * @throws IllegalStateException if the object does not have an ID.
     */
    default @NotNull ID requireId() {
        var id = id();
        if (id == null) {
            throw new IllegalStateException("No ID available");
        }
        return id;
    }
}
