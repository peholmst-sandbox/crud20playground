package org.vaadin.playground.crud20.router.param;

import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.Nonnull;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public final class RouteParamDefinition<T> implements Serializable {

    private final String name;
    private final SerializableFunction<String, T> deserializer;
    private final SerializableFunction<T, String> serializer;

    private RouteParamDefinition(@Nonnull String name,
                                 @Nonnull SerializableFunction<String, T> deserializer,
                                 @Nonnull SerializableFunction<T, String> serializer) {
        this.name = name;
        this.deserializer = deserializer;
        this.serializer = serializer;
    }

    public @Nonnull RouteParam toRouteParam(@Nonnull T value) {
        return new RouteParam(name, serializer.apply(value));
    }

    public @Nonnull Optional<T> getParameterValue(@Nonnull RouteParameters routeParameters) {
        try {
            return routeParameters.get(name).map(deserializer);
        } catch (IllegalArgumentException ex) {
            LoggerFactory.getLogger(RouteParamDefinition.class)
                    .warn("Failed to parse route parameter '{}': {}: {}", name, ex.getClass().getSimpleName(), ex.getMessage());
            return Optional.empty();
        }
    }

    public static RouteParamDefinition<Long> forLong(@Nonnull String name) {
        return new RouteParamDefinition<>(name, Long::parseLong, Objects::toString);
    }

    public static RouteParamDefinition<String> forString(@Nonnull String name) {
        return new RouteParamDefinition<>(name, s -> s, s -> s);
    }

    public static <T> RouteParamDefinition<T> forObject(@Nonnull String name,
                                                        @Nonnull SerializableFunction<String, T> deserializer,
                                                        @Nonnull SerializableFunction<T, String> serializer) {
        return new RouteParamDefinition<>(name, deserializer, serializer);
    }

    @Override
    public String toString() {
        return "RouteParamDefinition{name='" + name + "'}";
    }
}
