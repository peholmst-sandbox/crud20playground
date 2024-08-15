package org.vaadin.playground.crud20.demo.common.domain.model;

import net.pkhapps.commons.domain.primitives.IpAddress;
import net.pkhapps.commons.domain.primitives.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * An audit log entry representing an action that has been performed against an entity.
 *
 * @param entityId  the ID of the entity that the action was performed against.
 * @param timestamp the timestamp when the action was performed.
 * @param action    the action that was performed.
 * @param user      the user who performed the action, if available.
 * @param ipAddress the IP address from which the action was performed, if available.
 * @param <ID>      the type of the ID of the entity that the action was performed against.
 */
public record AuditLogEntry<ID>(@NotNull ID entityId,
                                @NotNull Instant timestamp,
                                @NotNull String action,
                                @Nullable UserId user,
                                @Nullable IpAddress ipAddress) {
}
