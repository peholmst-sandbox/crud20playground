package org.vaadin.playground.crud20.demo.common.domain.model;

/**
 * Interface to be implemented by entities.
 *
 * @param <ID> the type of the entity ID.
 */
public interface Entity<ID> extends HasId<ID> {

    /**
     * Returns whether this entity is new (still not saved in a repository) or persistent.
     */
    default boolean isNew() {
        return id() == null;
    }
}
