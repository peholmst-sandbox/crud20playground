package org.vaadin.playground.crud20.data.property.conversion;

import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableConsumer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.property.AbstractWritableProperty;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;
import org.vaadin.playground.crud20.data.property.validation.ValidationState;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class PropertyConverter<T, S> extends AbstractWritableProperty<T> implements ConvertedProperty<T> {

    private final WritableProperty<S> source;
    private final Converter<T, S> converter;
    private final WritableProperty<ValidationState> validationState = WritableProperty.create(new ValidationState.Unknown());
    private final T emptyValue;
    @SuppressWarnings("FieldCanBeLocal") // If used as a local field, the weak listener will be GC:d too early
    private final SerializableConsumer<PropertyValueChangeEvent<S>> onSourceValueChangeEvent = (event) -> updateLocalValue(event.value(), event.isEmpty());
    private ValueContext valueContext = new ValueContext();

    public PropertyConverter(@Nonnull WritableProperty<S> source, @Nonnull Converter<T, S> converter) {
        this.source = requireNonNull(source);
        this.converter = requireNonNull(converter);
        this.emptyValue = converter.convertToPresentation(source.emptyValue(), valueContext);
        source.addWeakListener(onSourceValueChangeEvent);
        updateLocalValue(source.value(), source.isEmpty());
    }

    @Nonnull
    public static <S, T> ConvertedProperty<T> of(@Nonnull WritableProperty<S> property, @Nonnull Converter<T, S> converter) {
        return new PropertyConverter<>(property, converter);
    }

    private void updateLocalValue(S sourceValue, boolean isEmpty) {
        if (isEmpty) {
            doSet(emptyValue, false);
        } else {
            doSet(converter.convertToPresentation(sourceValue, valueContext), false);
        }
        validationState.set(new ValidationState.Success());
    }

    @Nonnull
    @Override
    public ConvertedProperty<T> withValueContext(@Nullable ValueContext valueContext) {
        this.valueContext = Objects.requireNonNullElseGet(valueContext, ValueContext::new);
        return this;
    }

    @Nullable
    @Override
    public T emptyValue() {
        return emptyValue;
    }

    @Override
    public void set(T value, boolean force) {
        if (doSet(value, force)) {
            log.trace("Attempting conversion of value [{}]", value);
            converter.convertToModel(value, valueContext).handle(
                    successfulValue -> {
                        log.trace("Conversion was successful: [{}]", successfulValue);
                        validationState.set(new ValidationState.Success());
                        log.trace("Updating source property {} to [{}]", source, successfulValue);
                        source.set(successfulValue);
                    },
                    error -> {
                        log.trace("Conversion failed: {}", error);
                        validationState.set(new ValidationState.Failure(ErrorLevel.ERROR, error));
                    }
            );
        }
    }

    @Nonnull
    @Override
    public Property<ValidationState> validationState() {
        return validationState;
    }
}
