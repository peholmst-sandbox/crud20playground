package org.vaadin.playground.crud20.demo.zipcodestack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import net.pkhapps.commons.domain.primitives.geo.Address;
import net.pkhapps.commons.domain.primitives.geo.usa.USPostalAddress;
import net.pkhapps.commons.domain.primitives.geo.usa.USStateAndTerritory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.vaadin.playground.crud20.demo.address.AddressValidationResult;
import org.vaadin.playground.crud20.demo.address.spi.AddressValidator;

import java.time.Duration;
import java.util.Locale;
import java.util.Optional;

/**
 * Implementation of {@link AddressValidator} that uses the free zipcodestack.com web service to validate the
 * city, state and zip-code combinations of {@link USPostalAddress} objects.
 */
@Component
class USPostalAddressValidator implements AddressValidator {

    private final RestClient restClient;

    USPostalAddressValidator(@Value("${application.zipcodestack.api-key}") String apiKey) {
        var requestFactorySettings = new ClientHttpRequestFactorySettings(
                Duration.ofSeconds(5),
                Duration.ofSeconds(5),
                (SslBundle) null);
        this.restClient = RestClient.builder()
                .requestFactory(ClientHttpRequestFactories.get(requestFactorySettings))
                .defaultHeader("apikey", apiKey)
                .baseUrl("https://api.zipcodestack.com")
                .build();
    }

    static @NotNull AddressValidationResult validateFromResponse(@NotNull USPostalAddress address, @NotNull JsonNode response) {
        var results = Optional.ofNullable(response.get("results")).map(n -> n.get(address.zipCode().toString())).orElse(NullNode.getInstance());
        if (results.isEmpty()) {
            return AddressValidationResult.warning("%s does not appear to be a valid US zip code.".formatted(address.zipCode()));
        } else {
            var firstHit = results.get(0);
            var expectedCity = firstHit.get("city_en").asText("");
            var expectedState = firstHit.get("state_code").asText("").toUpperCase();
            if (!address.state().name().equalsIgnoreCase(expectedState)) {
                return AddressValidationResult.warning("The zip code %s appears to be in %s, not in %s.".formatted(address.zipCode(), USStateAndTerritory.valueOf(expectedState).displayName(), address.state().displayName()));
            }
            if (!address.city().toString().equalsIgnoreCase(expectedCity)) {
                return AddressValidationResult.warning("The zip code %s appears to be in %s, not in %s.".formatted(address.zipCode(), expectedCity, address.city()));
            }
            return AddressValidationResult.ok();
        }
    }

    @Override
    public boolean supports(@NotNull Address address) {
        return address instanceof USPostalAddress;
    }

    @Override
    public @NotNull AddressValidationResult validate(@NotNull Address address, @Nullable Locale locale) {
        // TODO Locale ignored for now
        if (address instanceof USPostalAddress usPostalAddress) {
            return validate(usPostalAddress);
        } else {
            return AddressValidationResult.indeterminate();
        }
    }

    private @NotNull AddressValidationResult validate(@NotNull USPostalAddress address) {
        try {
            var response = restClient.get().uri(uri -> uri
                            .path("/v1/search")
                            .queryParam("codes", address.zipCode().toString())
                            .queryParam("country", "us")
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(JsonNode.class);
            if (response.getBody() != null) {
                return validateFromResponse(address, response.getBody());
            }
        } catch (RestClientException ex) {
            LoggerFactory.getLogger(USPostalAddressValidator.class).warn("Error while contacting Zipcodestack", ex);
        }
        return AddressValidationResult.indeterminate();
    }
}
