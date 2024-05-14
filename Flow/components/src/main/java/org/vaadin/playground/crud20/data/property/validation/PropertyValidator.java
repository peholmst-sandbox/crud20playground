package org.vaadin.playground.crud20.data.property.validation;

import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.function.SerializableConsumer;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class PropertyValidator<T> implements Serializable {

    private final WritableProperty<List<ValidationResult>> validationResult = WritableProperty.createWithEmptyValue(Collections.emptyList());
    private final Property<Boolean> hasError = validationResult.map(results -> results.stream().anyMatch(ValidationResult::isError), false);
    private final Property<ErrorLevel> errorLevel = validationResult.map(results -> results.stream().flatMap(r -> r.getErrorLevel().stream()).max(Comparator.comparing(ErrorLevel::intValue)).orElse(null));
    private final Property<String> errorMessage = validationResult.map(results -> results.stream().filter(r -> r.getErrorLevel().isPresent()).map(ValidationResult::getErrorMessage).collect(Collectors.joining("\n")), "");
    private final Property<T> property;
    private ValueContext valueContext = new ValueContext();
    private ValidationStrategy validationStrategy = ValidateOnChange.INSTANCE;
    @SuppressWarnings("FieldCanBeLocal")
    private final SerializableConsumer<PropertyValueChangeEvent<T>> onPropertyValueChange = (event) -> validationStrategy.propertyValueChanged(this);
    private final Map<ValidatorGroup, List<ValidatorEntry>> validators = new HashMap<>();
    public static final ValidatorGroup DEFAULT_VALIDATOR_GROUP = new ValidatorGroup() {
    };

    public PropertyValidator(@Nonnull Property<T> property) {
        this.property = property;
        property.addWeakListener(onPropertyValueChange);
    }

    public static <T> @Nonnull PropertyValidator<T> of(@Nonnull Property<T> property) {
        return new PropertyValidator<>(property);
    }

    // This class has no getters for the state variables, only Properties. It does not follow the same convention
    // as ConvertedProperty. Which convention makes more sense is up for debate.

    public @Nonnull Property<List<ValidationResult>> result() {
        return validationResult;
    }

    public @Nonnull Property<Boolean> hasError() {
        return hasError;
    }

    public @Nonnull Property<ErrorLevel> errorLevel() {
        return errorLevel;
    }

    public @Nonnull Property<String> errorMessage() {
        return errorMessage;
    }

    public @Nonnull PropertyValidator<T> withValidator(@Nonnull Validator<T> validator) {
        return withValidator(validator, DEFAULT_VALIDATOR_GROUP);
    }

    public @Nonnull PropertyValidator<T> withValidator(@Nonnull Validator<T> validator, @Nonnull ValidatorGroup validatorGroup) {
        validators.computeIfAbsent(validatorGroup, key -> new ArrayList<>()).add(new ValidatorEntry(validator));
        validationStrategy.validatorAdded(this, validator, validatorGroup);
        return this;
    }

    public @Nonnull PropertyValidator<T> enableValidatorsOfType(@Nonnull Class<? extends Validator<?>> validatorClass) {
        enableValidators(getValidatorsOfType(validatorClass));
        return this;
    }

    public @Nonnull PropertyValidator<T> disableValidatorsOfType(@Nonnull Class<? extends Validator<?>> validatorClass) {
        disableValidators(getValidatorsOfType(validatorClass));
        return this;
    }

    private @Nonnull Collection<ValidatorEntry> getValidatorsOfType(@Nonnull Class<? extends Validator<?>> validatorClass) {
        return validators.values().stream()
                .flatMap(Collection::stream)
                .filter(entry -> validatorClass.isInstance(entry.validator))
                .toList();
    }

    public @Nonnull PropertyValidator<T> enableValidatorGroup(@Nonnull ValidatorGroup validatorGroup) {
        enableValidators(this.validators.getOrDefault(validatorGroup, Collections.emptyList()));
        return this;
    }

    public @Nonnull PropertyValidator<T> disableValidatorGroup(@Nonnull ValidatorGroup validatorGroup) {
        disableValidators(this.validators.getOrDefault(validatorGroup, Collections.emptyList()));
        return this;
    }

    private void enableValidators(@Nonnull Collection<ValidatorEntry> validators) {
        if (!validators.isEmpty()) {
            validators.forEach(validator -> validator.enabled = true);
            validationStrategy.validatorsEnabled(this, validators);
        }
    }

    private void disableValidators(@Nonnull Collection<ValidatorEntry> validators) {
        if (!validators.isEmpty()) {
            validators.forEach(validator -> validator.enabled = false);
            validationStrategy.validatorsDisabled(this, validators);
        }
    }

    public @Nonnull PropertyValidator<T> withValueContext(@Nonnull ValueContext valueContext) {
        this.valueContext = requireNonNull(valueContext);
        return this;
    }

    public @Nonnull PropertyValidator<T> withValidationStrategy(@Nonnull ValidationStrategy validationStrategy) {
        this.validationStrategy = requireNonNull(validationStrategy);
        return this;
    }

    public void validate() {
        validateGroups(validators.keySet().stream().sorted((o1, o2) -> {
            if (o1 == DEFAULT_VALIDATOR_GROUP) {
                return -1;
            } else if (o2 == DEFAULT_VALIDATOR_GROUP) {
                return 1;
            }
            return o1.compareTo(o2);
        }).toList());
    }

    public void validateGroups(@Nonnull ValidatorGroup... validatorGroups) {
        validateGroups(List.of(validatorGroups));
    }

    public void validateGroups(@Nonnull List<ValidatorGroup> validatorGroups) {
        validate(validatorGroups.stream()
                .flatMap(group -> validators.getOrDefault(group, Collections.emptyList()).stream())
                .filter(entry -> entry.enabled)
                .map(entry -> entry.validator)
                .toList()
        );
    }

    public void clear() {
        validationResult.set(Collections.emptyList());
    }

    private void validate(@Nonnull Collection<Validator<T>> validators) {
        var value = property.value();
        validationResult.set(validators.stream().map(validator -> validator.apply(value, valueContext)).toList());
    }

    private class ValidatorEntry implements Serializable {
        private final Validator<T> validator;
        private boolean enabled = true;

        public ValidatorEntry(@Nonnull Validator<T> validator) {
            this.validator = validator;
        }
    }

    public interface ValidationStrategy extends Serializable {

        default void validatorAdded(@Nonnull PropertyValidator<?> propertyValidator, @Nonnull Validator<?> validator, @Nonnull ValidatorGroup validatorGroup) {
            // NOP
        }

        default void validatorsEnabled(@Nonnull PropertyValidator<?> propertyValidator, @Nonnull Collection<?> enabledValidators) {
            // NOP
        }

        default void validatorsDisabled(@Nonnull PropertyValidator<?> propertyValidator, @Nonnull Collection<?> disabledValidators) {
            // NOP
        }

        void propertyValueChanged(@Nonnull PropertyValidator<?> propertyValidator);
    }

    public static final class ValidateOnChange implements ValidationStrategy {

        public static final ValidateOnChange INSTANCE = new ValidateOnChange();

        @Override
        public void propertyValueChanged(@Nonnull PropertyValidator<?> propertyValidator) {
            propertyValidator.validate();
        }

        // I'm not sure about triggering validation like this when validators are disabled and enabled.

        @Override
        public void validatorsDisabled(@Nonnull PropertyValidator<?> propertyValidator, @Nonnull Collection<?> disabledValidators) {
            if (propertyValidator.result().isPresent()) {
                propertyValidator.validate();
            }
        }

        @Override
        public void validatorsEnabled(@Nonnull PropertyValidator<?> propertyValidator, @Nonnull Collection<?> enabledValidators) {
            if (propertyValidator.result().isPresent()) {
                propertyValidator.validate();
            }
        }
    }

    public static final class ValidateOnCommand implements ValidationStrategy {

        public static final ValidateOnCommand INSTANCE = new ValidateOnCommand();

        @Override
        public void propertyValueChanged(@Nonnull PropertyValidator<?> propertyValidator) {
            // NOP; need to call validate() manually.
        }
    }
}
