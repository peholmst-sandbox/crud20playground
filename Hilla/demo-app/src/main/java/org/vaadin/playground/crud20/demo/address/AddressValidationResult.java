package org.vaadin.playground.crud20.demo.address;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO Document me!
 */
public sealed interface AddressValidationResult {

    static @NotNull AddressValidationResult ok() {
        return new OK();
    }

    static @NotNull AddressValidationResult indeterminate() {
        return new Indeterminate();
    }

    static @NotNull AddressValidationResult warning(@NotNull String message) {
        return new Warning(List.of(message));
    }

    @NotNull AddressValidationResult merge(@NotNull AddressValidationResult validationResult);

    record OK() implements AddressValidationResult {

        @Override
        public @NotNull AddressValidationResult merge(@NotNull AddressValidationResult validationResult) {
            if (validationResult instanceof Warning warning) {
                return warning;
            } else {
                return this;
            }
        }
    }

    record Indeterminate() implements AddressValidationResult {

        @Override
        public @NotNull AddressValidationResult merge(@NotNull AddressValidationResult validationResult) {
            if (validationResult instanceof Warning warning) {
                return warning;
            } else if (validationResult instanceof OK ok) {
                return ok;
            } else {
                return this;
            }
        }
    }

    record Warning(@NotNull Collection<String> messages) implements AddressValidationResult {

        public Warning(@NotNull Collection<String> messages) {
            if (messages.isEmpty()) {
                throw new IllegalArgumentException("At least one message must be provided");
            }
            this.messages = List.copyOf(messages);
        }

        @Override
        public @NotNull AddressValidationResult merge(@NotNull AddressValidationResult validationResult) {
            if (validationResult instanceof Warning otherWarning) {
                var mergedList = new ArrayList<String>(messages.size() + otherWarning.messages.size());
                mergedList.addAll(messages);
                mergedList.addAll(otherWarning.messages);
                return new Warning(mergedList);
            }
            return this;
        }
    }
}
