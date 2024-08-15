package org.vaadin.playground.crud20.demo.common.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining a repository of {@linkplain AggregateRoot aggregate roots}.
 *
 * @param <ID> the type of the aggregate root ID.
 * @param <E>  the type of the aggregate root.
 */
public interface Repository<ID, E extends AggregateRoot<ID>> {

    /**
     * Finds the aggregate root with the given ID.
     *
     * @param id the ID of the aggregate root to find.
     * @return the aggregate root, or an empty {@code Optional} if not found.
     */
    @NotNull Optional<E> findById(@NotNull ID id);

    /**
     * Saves the given aggregate root into the repository. If the aggregate root is
     * {@linkplain AggregateRoot#isNew() new}, a new entry is inserted into repository. If the aggregate root is not new,
     * the existing entry with the same ID as the aggregate root is updated. If no such entry is found, an exception is thrown.
     *
     * @param aggregateRoot the aggregate root to save.
     * @return the saved aggregate root, which may contain updated information like IDs and optimistic locking version numbers.
     */
    @NotNull E save(@NotNull E aggregateRoot);

    /**
     * Deletes the aggregate root with the given ID from the repository. If the aggregate root does not exist, nothing
     * happens.
     *
     * @param id the ID of the aggregate root to delete.
     */
    void deleteById(@NotNull ID id);

    /**
     * Checks if the repository contains an aggregate root with the given ID.
     *
     * @param id the ID of the aggregate root to check.
     * @return {@code true} if the repository contains the aggregate root, {@code false} otherwise.
     */
    boolean containsById(@NotNull ID id);

    /**
     * Gets the number of aggregate roots currently stored in the repository.
     *
     * @return the size of the repository.
     */
    long size();

    /**
     * Gets the audit log entries for the aggregate root with the given ID. If no entries exist for the given ID,
     * an empty list is returned.
     *
     * @param id the ID of the aggregate root to check.
     * @return a list of audit log entries.
     */
    @NotNull List<AuditLogEntry<ID>> findAuditLogById(@NotNull ID id);
}
