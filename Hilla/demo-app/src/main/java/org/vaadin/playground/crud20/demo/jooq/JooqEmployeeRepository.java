package org.vaadin.playground.crud20.demo.jooq;

import net.pkhapps.commons.domain.primitives.NanoId;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.vaadin.playground.crud20.demo.common.context.SessionContext;
import org.vaadin.playground.crud20.demo.common.domain.AuditLogEntry;
import org.vaadin.playground.crud20.demo.employee.domain.Employee;
import org.vaadin.playground.crud20.demo.employee.domain.spi.EmployeeRepository;
import org.vaadin.playground.crud20.demo.employee.domain.EmployeeId;

import java.util.List;
import java.util.Optional;

import static org.vaadin.playground.crud20.demo.jooq.tables.Employee.EMPLOYEE;
import static org.vaadin.playground.crud20.demo.jooq.tables.EmployeeAuditLog.EMPLOYEE_AUDIT_LOG;

/**
 * Implementation of {@link EmployeeRepository} that uses JOOQ to store employees in a SQL database.
 */
@Repository
class JooqEmployeeRepository implements EmployeeRepository {

    private final DSLContext dslContext;
    private final SessionContext sessionContext;

    JooqEmployeeRepository(DSLContext dslContext, SessionContext sessionContext) {
        this.dslContext = dslContext.configuration()
                .deriveSettings(settings -> settings.withExecuteWithOptimisticLocking(true))
                .dsl();
        this.sessionContext = sessionContext;
    }

    @Override
    public @NotNull Optional<Employee> findById(@NotNull EmployeeId internalId) {
        return dslContext.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.EMPLOYEE_ID.eq(internalId))
                .fetch(Records.mapping(Employee::new))
                .stream().findFirst();
    }

    @Override
    public boolean containsById(@NotNull EmployeeId internalId) {
        return dslContext.selectCount().from(EMPLOYEE).where(EMPLOYEE.EMPLOYEE_ID.eq(internalId)).fetchSingle().value1() == 1;
    }

    @Override
    public @NotNull Optional<Employee> findByPublicId(@NotNull NanoId publicId) {
        return dslContext.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.PUBLIC_EMPLOYEE_ID.eq(publicId))
                .fetch(Records.mapping(Employee::new))
                .stream().findFirst();
    }

    @Override
    public boolean containsByPublicId(@NotNull NanoId publicId) {
        return dslContext.selectCount().from(EMPLOYEE).where(EMPLOYEE.PUBLIC_EMPLOYEE_ID.eq(publicId)).fetchSingle().value1() == 1;
    }

    @Override
    public @NotNull Optional<EmployeeId> resolveId(@NotNull NanoId publicId) {
        return resolveId(dslContext, publicId);
    }

    private @NotNull Optional<EmployeeId> resolveId(@NotNull DSLContext dslContext, @NotNull NanoId publicId) {
        return dslContext.dsl().select(EMPLOYEE.EMPLOYEE_ID).from(EMPLOYEE).where(EMPLOYEE.PUBLIC_EMPLOYEE_ID.eq(publicId)).fetchOptional(EMPLOYEE.EMPLOYEE_ID);
    }

    @Override
    public @NotNull Employee save(@NotNull Employee aggregateRoot) {
        if (aggregateRoot.isNew()) {
            return insert(aggregateRoot);
        } else {
            return update(aggregateRoot);
        }
    }

    @Override
    public void deleteById(@NotNull EmployeeId id) {
        dslContext.transaction(trx -> deleteById(trx.dsl(), id));
    }

    private void deleteById(@NotNull DSLContext dslContext, @NotNull EmployeeId id) {
        if (dslContext.deleteFrom(EMPLOYEE).where(EMPLOYEE.EMPLOYEE_ID.eq(id)).execute() > 0) {
            JooqUtils.insertIntoAuditLog(sessionContext, dslContext, EMPLOYEE_AUDIT_LOG, id, "DELETE");
        }
    }

    @Override
    public void deleteByPublicId(@NotNull NanoId publicId) {
        dslContext.transaction(trx -> {
            resolveId(trx.dsl(), publicId).ifPresent(id -> deleteById(trx.dsl(), id));
        });
    }

    @Override
    public long size() {
        var count = DSL.field("count(*)", SQLDataType.BIGINT);
        return dslContext.select(count).from(EMPLOYEE).fetchSingle().value1();
    }

    @Override
    public @NotNull List<AuditLogEntry<EmployeeId>> findAuditLogById(@NotNull EmployeeId employeeId) {
        return dslContext.selectFrom(EMPLOYEE_AUDIT_LOG)
                .where(EMPLOYEE_AUDIT_LOG.EMPLOYEE_ID.eq(employeeId))
                .orderBy(EMPLOYEE_AUDIT_LOG.EMPLOYEE_AUDIT_LOG_ID)
                .fetch(JooqUtils::mapAuditLogRecord);
    }

    private @NotNull Employee insert(@NotNull Employee employee) {
        return JooqUtils.retryOnIntegrityConstraintViolation(() -> dslContext.transactionResult(trx -> {
            var record = trx.dsl().newRecord(EMPLOYEE);
            record.from(employee);
            record.setPublicEmployeeId(NanoId.randomNanoId());
            record.insert();

            JooqUtils.insertIntoAuditLog(sessionContext, trx.dsl(), EMPLOYEE_AUDIT_LOG, record.getEmployeeId(), "INSERT");

            return record.into(Employee.class);
        }), 3);

    }

    private @NotNull Employee update(@NotNull Employee employee) {
        return dslContext.transactionResult(trx -> {
            var record = trx.dsl().selectFrom(EMPLOYEE).where(EMPLOYEE.EMPLOYEE_ID.eq(employee.requireId())).fetchSingle();
            record.from(employee);
            record.update();

            JooqUtils.insertIntoAuditLog(sessionContext, trx.dsl(), EMPLOYEE_AUDIT_LOG, record.getEmployeeId(), "UPDATE");

            return record.into(Employee.class);
        });
    }
}
