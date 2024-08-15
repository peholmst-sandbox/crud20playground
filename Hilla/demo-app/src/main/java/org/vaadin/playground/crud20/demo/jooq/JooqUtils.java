package org.vaadin.playground.crud20.demo.jooq;

import net.pkhapps.commons.domain.primitives.IpAddress;
import net.pkhapps.commons.domain.primitives.UserId;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.Table;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.vaadin.playground.crud20.demo.common.context.SessionContext;
import org.vaadin.playground.crud20.demo.common.domain.AuditLogEntry;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

final class JooqUtils {

    // TODO This audit log does not contain much information about what was actually changed, only that something was changed.

    private JooqUtils() {
    }

    public static <T> T retryOnIntegrityConstraintViolation(@NotNull Supplier<T> job, int numberOfRetries) {
        try {
            return job.get();
        } catch (IntegrityConstraintViolationException e) {
            if (numberOfRetries > 0) {
                return retryOnIntegrityConstraintViolation(job, numberOfRetries - 1);
            } else {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <ID, R extends Record6<Long, ID, String, OffsetDateTime, UserId, IpAddress>> void insertIntoAuditLog(@NotNull SessionContext sessionContext,
                                                                                                                       @NotNull DSLContext dslContext,
                                                                                                                       @NotNull Table<R> table,
                                                                                                                       @NotNull ID internalId,
                                                                                                                       @NotNull String action) {
        dslContext.insertInto(table,
                        (Field<ID>) table.field(1),
                        (Field<String>) table.field(2),
                        (Field<OffsetDateTime>) table.field(3),
                        (Field<UserId>) table.field(4),
                        (Field<IpAddress>) table.field(5)
                )
                .values(
                        internalId,
                        action,
                        sessionContext.now(),
                        sessionContext.currentUser().orElse(null),
                        sessionContext.clientIpAddress().orElse(null)
                )
                .execute();
    }

    public static <ID, R extends Record6<Long, ID, String, OffsetDateTime, UserId, IpAddress>> @NotNull AuditLogEntry<ID> mapAuditLogRecord(@NotNull R record) {
        return new AuditLogEntry<>(record.value2(), record.value4().toInstant(), record.value3(), record.value5(), record.value6());
    }
}
