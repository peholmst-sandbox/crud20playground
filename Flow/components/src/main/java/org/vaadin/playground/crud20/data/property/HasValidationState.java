package org.vaadin.playground.crud20.data.property;

import jakarta.annotation.Nonnull;

public interface HasValidationState {

    @Nonnull
    Property<ValidationState> validationState();
}
