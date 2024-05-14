package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.data.binder.ValueContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface ConvertedProperty<T> extends WritableProperty<T> {

    @Nonnull
    ConvertedProperty<T> withValueContext(@Nullable ValueContext valueContext);

    // Not sure whether it makes sense to have the conversion state available both as a property and a method.

    @Nonnull
    ConversionState conversionState();

    @Nonnull
    Property<ConversionState> conversionStateProperty();

    sealed interface ConversionState {

        default boolean isError() {
            return this instanceof Failure;
        }

        record Success() implements ConversionState {
        }

        record Failure(String errorMessage) implements ConversionState {
        }
    }
}