package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.WritableProperty;

class WritablePropertyHasValueBinding<T> implements PropertyBinding {

    private final Registration propertyRegistration;
    private final Registration hasValueRegistration;

    public WritablePropertyHasValueBinding(@Nonnull WritableProperty<T> property,
                                           @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        if (property.isEmpty()) {
            hasValue.clear();
        } else {
            hasValue.setValue(property.value());
        }
        propertyRegistration = property.addListener(event -> {
            if (event.isEmpty()) {
                hasValue.clear();
            } else {
                hasValue.setValue(event.value());
            }
        });
        hasValueRegistration = hasValue.addValueChangeListener(event -> {
            if (event.getHasValue().isEmpty()) {
                property.clear();
            } else {
                property.set(event.getValue());
            }
        });
    }

    @Override
    public void remove() {
        propertyRegistration.remove();
        hasValueRegistration.remove();
    }
}
