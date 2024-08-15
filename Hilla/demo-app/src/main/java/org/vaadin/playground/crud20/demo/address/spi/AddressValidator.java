package org.vaadin.playground.crud20.demo.address.spi;

import net.pkhapps.commons.domain.primitives.geo.Address;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vaadin.playground.crud20.demo.address.AddressValidationResult;

import java.util.Locale;

/**
 * SPI for plugging into the {@link org.vaadin.playground.crud20.demo.address.AddressValidationService}.
 */
public interface AddressValidator {

    /**
     * Checks whether this validator can validate the given address.
     *
     * @param address the address to check.
     * @return {@code true} if the validator can validate the address, {@code false} otherwise.
     */
    boolean supports(@NotNull Address address);

    /**
     * Attempts to validate the given address. If the validator cannot validate the address for some reason,
     * it should return an {@linkplain AddressValidationResult#indeterminate() indeterminate result}.
     *
     * @param address the address to validate.
     * @param locale  the locale to display the warning messages in.
     * @return the validation result.
     */
    @NotNull AddressValidationResult validate(@NotNull Address address, @Nullable Locale locale);
}
