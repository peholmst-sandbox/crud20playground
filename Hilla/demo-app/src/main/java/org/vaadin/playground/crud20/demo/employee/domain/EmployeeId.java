package org.vaadin.playground.crud20.demo.employee.domain;

import net.pkhapps.commons.domain.primitives.AbstractLongId;
import org.jetbrains.annotations.NotNull;

public final class EmployeeId extends AbstractLongId {

    private EmployeeId(long value) {
        super(value);
    }

    public static @NotNull EmployeeId valueOf(long value) {
        return new EmployeeId(value);
    }
}
