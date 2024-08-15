package org.vaadin.playground.crud20.demo.common.domain.model;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Repository interface for aggregate roots that have a separate public ID in addition to its "internal" ID. Although
 * the interface provides some default implementations, implementations of this interface may want to override them
 * to improve performance or change how transactions are handled.
 *
 * @param <ID>  the type of the aggregate root "internal" ID.
 * @param <PID> the type of the aggregate root public ID.
 * @param <E>   the type of the aggregate root.
 */
public interface HasPublicIdRepository<ID, PID, E extends AggregateRoot<ID> & HasPublicId<PID>> extends Repository<ID, E> {

    /**
     * Finds the aggregate root with the given public ID.
     *
     * @param publicId the public ID of the aggregate root to find.
     * @return the aggregate root, or an empty {@code Optional} if not found.
     */
    default @NotNull Optional<E> findByPublicId(@NotNull PID publicId) {
        return resolveId(publicId).flatMap(this::findById);
    }

    /**
     * Deletes the aggregate root with the given public ID from the repository. If the aggregate root does not exist,
     * nothing happens.
     *
     * @param publicId the public ID of the aggregate root to delete.
     */
    default void deleteByPublicId(@NotNull PID publicId) {
        resolveId(publicId).ifPresent(this::deleteById);
    }

    /**
     * Checks if the repository contains an aggregate root with the given public ID.
     *
     * @param publicId the public ID of the aggregate root to check.
     * @return {@code true} if the repository contains the aggregate root, {@code false} otherwise.
     */
    default boolean containsByPublicId(@NotNull PID publicId) {
        return resolveId(publicId).isPresent();
    }

    /**
     * Looks up the "internal" ID of the aggregate root with the given public ID.
     *
     * @param publicId the public ID of the aggregate root.
     * @return the "internal" ID of the aggregate root, or an empty {@code Optional} if not found.
     */
    @NotNull Optional<ID> resolveId(@NotNull PID publicId);
}
