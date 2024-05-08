package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableConsumer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

class DefaultConvertedProperty<T, S> extends AbstractWritableProperty<T> implements ConvertedProperty<T> {

    private final AbstractWritableProperty<S> source;
    private final Converter<T, S> converter;
    private final WritableProperty<ConversionState> conversionState = WritableProperty.create();
    private final T emptyValue;
    @SuppressWarnings("FieldCanBeLocal") // If used as a local field, the weak listener will be GC:d too early
    private final SerializableConsumer<PropertyValueChangeEvent<S>> onSourceValueChangeEvent = (event) -> updateLocalValue(event.value(), event.isEmpty());
    private ValueContext valueContext = new ValueContext();

    public DefaultConvertedProperty(@Nonnull AbstractWritableProperty<S> source, @Nonnull Converter<T, S> converter) {
        this.source = requireNonNull(source);
        this.converter = requireNonNull(converter);
        this.emptyValue = converter.convertToPresentation(source.emptyValue(), valueContext);
        source.addWeakListener(onSourceValueChangeEvent);
        updateLocalValue(source.value(), source.isEmpty());
    }

    private void updateLocalValue(S sourceValue, boolean isEmpty) {
        if (isEmpty) {
            doSet(emptyValue);
        } else {
            doSet(converter.convertToPresentation(sourceValue, valueContext));
        }
        conversionState.set(new ConversionState.Success());
    }

    @Nonnull
    @Override
    public ConvertedProperty<T> withValueContext(@Nullable ValueContext valueContext) {
        this.valueContext = Objects.requireNonNullElseGet(valueContext, ValueContext::new);
        return this;
    }

    @Nonnull
    @Override
    public ConversionState conversionState() {
        return conversionState.value();
    }

    @Nonnull
    @Override
    public Property<ConversionState> conversionStateProperty() {
        return conversionState;
    }

    @Nullable
    @Override
    public T emptyValue() {
        return emptyValue;
    }

    @Override
    public void set(T value) {
        doSet(value);
        converter.convertToModel(value, valueContext).handle(
                successfulValue -> {
                    source.set(successfulValue);
                    conversionState.set(new ConversionState.Success());
                },
                error -> conversionState.set(new ConversionState.Failure(error))
        );
    }
}
