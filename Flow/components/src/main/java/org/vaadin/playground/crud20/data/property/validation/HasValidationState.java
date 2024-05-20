package org.vaadin.playground.crud20.data.property.validation;

import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;

public interface HasValidationState {

    @Nonnull
    Property<ValidationState> validationState();
}
