package org.vaadin.playground.crud20.demo.sampledata;

import java.io.Serializable;
import java.util.Objects;

public final class EmployeeId implements Serializable {

    private final long id;

    public EmployeeId(long id) {
        this.id = id;
    }

    public EmployeeId(String id) {
        this(Long.parseLong(id));
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeId that = (EmployeeId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
