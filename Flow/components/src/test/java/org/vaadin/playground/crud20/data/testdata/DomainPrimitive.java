package org.vaadin.playground.crud20.data.testdata;

import java.util.Objects;

public record DomainPrimitive(String value) {

    public DomainPrimitive {
        Objects.requireNonNull(value);
        if (value.equals("fail")) {
            throw new IllegalArgumentException("The value was incorrect");
        }
    }
}
