package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.validation.PropertyValidator;

class ValidationResultBinding implements PropertyBinding {

    private final Registration hasErrorRegistration;
    private final Registration errorMessageRegistration;

    public ValidationResultBinding(@Nonnull PropertyValidator<?> validator,
                                   @Nonnull HasValidation hasValidation) {
        hasErrorRegistration = validator.hasError().triggerIfPresent(hasValidation::setInvalid);
        errorMessageRegistration = validator.errorMessage().triggerIfPresent(hasValidation::setErrorMessage);
    }

    @Override
    public void remove() {
        hasErrorRegistration.remove();
        errorMessageRegistration.remove();
    }
}
