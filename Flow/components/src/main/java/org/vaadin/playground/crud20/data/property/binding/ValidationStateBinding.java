package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.ValidationState;

import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;

class ValidationStateBinding implements PropertyBinding {

    private final List<Property<ValidationState>> properties;
    private List<Registration> registrations;
    private final HasValidation hasValidation;
    private boolean enabled = true;

    public ValidationStateBinding(@Nonnull List<Property<ValidationState>> properties,
                                  @Nonnull HasValidation hasValidation) {
        this.hasValidation = requireNonNull(hasValidation);
        if (properties.isEmpty()) {
            throw new IllegalArgumentException("At least one property must be provided");
        }
        this.properties = properties;
        bind();
    }

    private void updateValidationState() {
        if (!enabled) {
            return;
        }
        var highestError = properties
                .stream()
                .map(Property::value)
                .filter(ValidationState.Failure.class::isInstance)
                .map(ValidationState.Failure.class::cast)
                .max(Comparator.comparingInt(a -> a.errorLevel().intValue()));
        highestError.ifPresentOrElse(failure -> {
            hasValidation.setErrorMessage(failure.errorMessage());
            hasValidation.setInvalid(failure.isError());
        }, () -> {
            hasValidation.setErrorMessage(null);
            hasValidation.setInvalid(false);
        });
    }

    @Override
    public void remove() {
        if (registrations != null) {
            registrations.forEach(Registration::remove);
            registrations = null;
        }
    }

    @Override
    public void bind() {
        if (this.registrations == null) {
            this.registrations = properties.stream().map(p -> p.addListener(event -> updateValidationState())).toList();
            updateValidationState();
        }
    }

    @Override
    public void enable() {
        this.enabled = true;
        updateValidationState();
    }

    @Override
    public void disable() {
        this.enabled = false;
    }
}
