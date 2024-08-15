package org.vaadin.playground.crud20.demo.common.domain.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to be implemented by classes that have an ID that is intended for public use (such as a NanoId).
 *
 * @param <PID> the type of the public ID.
 */
public interface HasPublicId<PID> {

    /**
     * Returns the object's public ID, or {@code null} if the object does not have one.
     */
    @Nullable PID publicId();

    /**
     * Returns the object's public ID.
     *
     * @throws IllegalStateException if the object does not have a public ID.
     */
    default @NotNull PID requirePublicId() {
        var publicId = publicId();
        if (publicId == null) {
            throw new IllegalStateException("No public ID available");
        }
        return publicId;
    }
}
