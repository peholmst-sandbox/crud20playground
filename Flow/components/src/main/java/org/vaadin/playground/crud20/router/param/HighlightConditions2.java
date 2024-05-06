package org.vaadin.playground.crud20.router.param;

import com.vaadin.flow.router.HighlightCondition;

public final class HighlightConditions2 {

    private HighlightConditions2() {
        // Utility class
    }

    public static <T, E> HighlightCondition<E> paramEquals(RouteParamDefinition<T> definition, T value) {
        return (o, event) -> definition.getParameterValue(event.getRouteParameters()).map(value::equals).orElse(false);
    }
}
