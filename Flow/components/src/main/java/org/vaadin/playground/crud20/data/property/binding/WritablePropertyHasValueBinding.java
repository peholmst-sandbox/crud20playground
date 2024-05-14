package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import static java.util.Objects.requireNonNull;

class WritablePropertyHasValueBinding<T> implements PropertyBinding {

    private final Registration propertyRegistration;
    private final Registration hasValueRegistration;
    private final WritableProperty<T> property;
    private final HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue;
    private boolean enabled = true;

    public WritablePropertyHasValueBinding(@Nonnull WritableProperty<T> property,
                                           @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        this.property = requireNonNull(property);
        this.hasValue = requireNonNull(hasValue);
        updateHasValue();
        propertyRegistration = property.addListener(event -> {
            if (enabled) {
                updateHasValue();
            }
        });
        hasValueRegistration = hasValue.addValueChangeListener(event -> {
            if (enabled) {
                updateProperty();
            }
        });
    }

    private void updateHasValue() {
        if (property.isEmpty()) {
            hasValue.clear();
        } else {
            hasValue.setValue(property.value());
        }
    }

    private void updateProperty() {
        if (hasValue.isEmpty()) {
            property.clear();
        } else {
            property.set(hasValue.getValue());
        }
    }

    @Override
    public void remove() {
        propertyRegistration.remove();
        hasValueRegistration.remove();
    }

    @Override
    public void enable() {
        this.enabled = true;
        updateHasValue();
    }

    @Override
    public void disable() {
        this.enabled = false;
    }
}
