package org.vaadin.playground.crud20.data.property;

import com.vaadin.flow.data.binder.ErrorLevel;
import jakarta.annotation.Nonnull;

import java.io.Serializable;

public sealed interface ValidationState extends Serializable {

    // This is a bit problematic, because you can be in a state where both isUnknown, isSuccess and isError are all false.

    default boolean isUnknown() {
        return this instanceof Unknown;
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isError() {
        return this instanceof Failure failure && failure.errorLevel.intValue() >= ErrorLevel.ERROR.intValue();
    }

    record Unknown() implements ValidationState {
    }

    record Failure(@Nonnull ErrorLevel errorLevel, @Nonnull String errorMessage) implements ValidationState {
    }

    record Success() implements ValidationState {
    }
}
