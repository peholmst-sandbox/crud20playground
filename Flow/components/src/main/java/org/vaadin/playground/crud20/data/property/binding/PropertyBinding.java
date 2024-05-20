package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.WritableProperty;
import org.vaadin.playground.crud20.data.property.validation.HasValidationState;

import java.util.List;

public interface PropertyBinding extends Registration {

    default @Nonnull PropertyBinding removeOnDetach(@Nonnull Component component) {
        component.addDetachListener(event -> remove());
        return this;
    }

    default @Nonnull PropertyBinding rebindOnAttach(@Nonnull Component component) {
        component.addAttachListener(event -> bind());
        return this;
    }

    void bind();

    /**
     * @deprecated Use {@link #bind()} instead. This method was created when it was only possible to remove a binding, not re-bind it.
     */
    @Deprecated
    void enable();

    /**
     * @deprecated Use {@link #remove()} instead. This method was created when it was only possible to remove a binding, not re-bind it.
     */
    @Deprecated
    void disable();


    static <T> @Nonnull PropertyBinding bindValue(@Nonnull Property<T> property, @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        return new PropertyHasValueBinding<>(property, hasValue);
    }

    static <T> @Nonnull PropertyBinding bindValueBidirectionally(@Nonnull WritableProperty<T> property, @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        return new WritablePropertyHasValueBinding<>(property, hasValue);
    }

    static @Nonnull PropertyBinding bindValidationState(@Nonnull HasValidationState hasValidationState, @Nonnull HasValidation hasValidation) {
        return bindValidationState(List.of(hasValidationState), hasValidation);
    }


    static @Nonnull PropertyBinding bindValidationState(@Nonnull List<HasValidationState> hasValidationStates, @Nonnull HasValidation hasValidation) {
        return new ValidationStateBinding(hasValidationStates.stream().map(HasValidationState::validationState).toList(), hasValidation);
    }

    // TODO Make other bindings for enabled, readOnly, visible, CSS classNames, etc.
}
