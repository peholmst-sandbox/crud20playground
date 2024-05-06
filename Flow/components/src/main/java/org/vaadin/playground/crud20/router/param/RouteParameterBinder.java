package org.vaadin.playground.crud20.router.param;

import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public final class RouteParameterBinder implements Serializable {

    private final Set<SignalToRouteParamBinding<?>> bindings = new HashSet<>();
    private RouteParameters routeParameters = RouteParameters.empty();

    public void load(@Nonnull RouteParameters routeParameters) {
        this.routeParameters = routeParameters;
        Set.copyOf(bindings).forEach(binding -> binding.updateSignal(routeParameters));
    }

    public <T> @Nonnull Registration bindSignalToParameter(@Nonnull WritableProperty<T> signal, @Nonnull RouteParamDefinition<T> parameterDefinition) {
        var binding = new SignalToRouteParamBinding<>(signal, parameterDefinition);
        bindings.add(binding);
        binding.updateSignal(routeParameters);
        return () -> bindings.remove(binding);
    }

    private record SignalToRouteParamBinding<T>(@Nonnull WritableProperty<T> signal,
                                                @Nonnull RouteParamDefinition<T> parameterDefinition) {
        void updateSignal(@Nonnull RouteParameters routeParameters) {
            parameterDefinition.getParameterValue(routeParameters).ifPresentOrElse(signal::set, signal::clear);
        }
    }
}
