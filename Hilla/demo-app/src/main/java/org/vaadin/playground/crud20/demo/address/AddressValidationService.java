package org.vaadin.playground.crud20.demo.address;

import net.pkhapps.commons.domain.primitives.geo.Address;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.vaadin.playground.crud20.demo.address.spi.AddressValidator;

import java.util.Locale;

/**
 * Domain service for validating {@link Address} objects. The domain service uses an SPI to make it possible to plugin
 * different validators for different subtypes of {@link Address}.
 */
@Service
public class AddressValidationService {

    private final ObjectProvider<AddressValidator> addressValidators;

    public AddressValidationService(ObjectProvider<AddressValidator> addressValidators) {
        this.addressValidators = addressValidators;
    }

    /**
     * Validates the given address.
     *
     * @param address the address to validate.
     * @param locale  the locale to display the warning messages in.
     * @return the validation result.
     */
    public @NotNull AddressValidationResult validateAddress(@NotNull Address address, @Nullable Locale locale) {
        return addressValidators.stream()
                .filter(validator -> validator.supports(address))
                .map(validator -> validator.validate(address, locale))
                .reduce(AddressValidationResult.indeterminate(), AddressValidationResult::merge);
    }
}
