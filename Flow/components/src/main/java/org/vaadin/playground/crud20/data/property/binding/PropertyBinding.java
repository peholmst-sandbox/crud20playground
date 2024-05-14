package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.WritableProperty;

public interface PropertyBinding extends Registration {

    // Is there a need to enable and disable a binding as well?

//    static <T> @Nonnull PropertyBinding bindValue(@Nonnull Property<T> property, @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
//        return new PropertyHasValueBinding<>(property, hasValue);
//    }

    static <T> @Nonnull PropertyBinding bindValueBidirectionally(@Nonnull WritableProperty<T> property, @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        return new WritablePropertyHasValueBinding<>(property, hasValue);
    }

//    static @Nonnull PropertyBinding bindError(@Nonnull Property<String> property, @Nonnull HasValidation hasValidation) {
//        throw new UnsupportedOperationException("Not yet implemented"); // TODO Implement me
//    }

//    static @Nonnull PropertyBinding bindValidationResult(@Nonnull PropertyValidator<?> propertyValidator, @Nonnull HasValidation hasValidation) {
//        throw new UnsupportedOperationException("Not yet implemented"); // TODO Implement me
//    }
}
