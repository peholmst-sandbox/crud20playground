package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.data.binder.ValueContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface ConvertedProperty<T> extends WritableProperty<T>, HasValidationState {

    // TODO Make it possible to plug in a strategy for handling conversion errors.

    @Nonnull
    ConvertedProperty<T> withValueContext(@Nullable ValueContext valueContext);
}
