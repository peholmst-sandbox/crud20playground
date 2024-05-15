package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.WritableProperty;

class WritablePropertyHasValueBinding<T> extends PropertyHasValueBinding<T> {

    private Registration hasValueRegistration;

    public WritablePropertyHasValueBinding(@Nonnull WritableProperty<T> property,
                                           @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        super(property, hasValue);
    }

    private void updateProperty() {
        if (hasValue().isEmpty()) {
            property().clear();
        } else {
            property().set(hasValue().getValue());
        }
    }

    @Nonnull
    @Override
    protected WritableProperty<T> property() {
        return (WritableProperty<T>) super.property();
    }

    @Override
    public void remove() {
        super.remove();
        if (hasValueRegistration != null) {
            hasValueRegistration.remove();
            hasValueRegistration = null;
        }
    }

    @Override
    public void bind() {
        super.bind();
        if (hasValueRegistration == null) {
            hasValueRegistration = hasValue().addValueChangeListener(event -> {
                if (isEnabled()) {
                    updateProperty();
                }
            });
        }
    }
}
