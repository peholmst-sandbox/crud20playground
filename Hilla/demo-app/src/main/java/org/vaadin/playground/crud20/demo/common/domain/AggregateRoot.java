package org.vaadin.playground.crud20.demo.common.domain;

/**
 * Interface to be implemented by entities that also act as aggregate roots.
 *
 * @param <ID> the type of the entity ID.
 */
public interface AggregateRoot<ID> extends Entity<ID> {

    /**
     * Returns the version number to use for optimistic locking.
     */
    long optimisticLockingVersion();
}
