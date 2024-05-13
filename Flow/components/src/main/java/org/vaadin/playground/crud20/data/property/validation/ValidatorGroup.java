package org.vaadin.playground.crud20.data.property.validation;

import java.io.Serializable;

public interface ValidatorGroup extends Serializable, Comparable<ValidatorGroup> {

    default int compareTo(ValidatorGroup o) {
        return 0;
    }
}
