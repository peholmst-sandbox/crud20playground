package org.vaadin.playground.crud20.demo.jooq;

import org.jooq.impl.AbstractConverter;
import org.vaadin.playground.crud20.demo.employee.domain.EmployeeId;

public class EmployeeIdConverter extends AbstractConverter<Long, EmployeeId> {

    public EmployeeIdConverter() {
        super(Long.class, EmployeeId.class);
    }

    @Override
    public EmployeeId from(Long databaseObject) {
        return databaseObject == null ? null : EmployeeId.valueOf(databaseObject);
    }

    @Override
    public Long to(EmployeeId userObject) {
        return userObject == null ? null : userObject.toLong();
    }
}
