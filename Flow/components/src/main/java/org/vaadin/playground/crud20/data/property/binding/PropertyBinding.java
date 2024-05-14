package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.HasValidationState;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.util.List;

public interface PropertyBinding extends Registration {

    default @Nonnull PropertyBinding removeOnDetach(@Nonnull Component component) {
        component.addDetachListener(event -> remove());
        return this;
    }

    void enable();

    void disable();

    static <T> @Nonnull PropertyBinding bindValueBidirectionally(@Nonnull WritableProperty<T> property, @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        return new WritablePropertyHasValueBinding<>(property, hasValue);
    }

    static @Nonnull PropertyBinding bindValidationState(@Nonnull HasValidationState hasValidationState, @Nonnull HasValidation hasValidation) {
        return bindValidationState(List.of(hasValidationState), hasValidation);
    }


    static @Nonnull PropertyBinding bindValidationState(@Nonnull List<HasValidationState> hasValidationStates, @Nonnull HasValidation hasValidation) {
        return new ValidationStateBinding(hasValidationStates.stream().map(HasValidationState::validationState).toList(), hasValidation);
    }

    // Make other bindings for enabled, readOnly, visible, CSS classNames, etc.
}
