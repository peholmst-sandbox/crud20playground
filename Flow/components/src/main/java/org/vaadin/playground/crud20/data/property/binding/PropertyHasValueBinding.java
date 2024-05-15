package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;

import static java.util.Objects.requireNonNull;

class PropertyHasValueBinding<T> implements PropertyBinding {

    private Registration propertyRegistration;
    private final Property<T> property;
    private final HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue;
    private boolean enabled = true;

    public PropertyHasValueBinding(@Nonnull Property<T> property,
                                   @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue) {
        this.property = requireNonNull(property);
        this.hasValue = requireNonNull(hasValue);
        bind();
    }

    protected void updateHasValue() {
        if (property.isEmpty()) {
            hasValue.clear();
        } else {
            hasValue.setValue(property.value());
        }
    }

    @Override
    public void remove() {
        if (propertyRegistration != null) {
            propertyRegistration.remove();
            propertyRegistration = null;
        }
    }

    @Override
    public void bind() {
        if (propertyRegistration == null) {
            propertyRegistration = property.addListener(event -> {
                if (enabled) {
                    updateHasValue();
                }
            });
            updateHasValue();
        }
    }

    @Override
    @Deprecated
    public void enable() {
        this.enabled = true;
        updateHasValue();
    }

    @Override
    @Deprecated
    public void disable() {
        this.enabled = false;
    }

    @Deprecated
    protected boolean isEnabled() {
        return enabled;
    }

    protected @Nonnull HasValue<? extends HasValue.ValueChangeEvent<T>, T> hasValue() {
        return hasValue;
    }

    protected @Nonnull Property<T> property() {
        return property;
    }
}
