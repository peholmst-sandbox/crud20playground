package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableFunction;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LazySwitchingContainer<ID> extends Composite<Div> implements HasSize {

    private final Map<ID, Component> componentCache = new HashMap<>();
    private final SerializableFunction<ID, Component> componentFactory;
    private ID selection;

    public LazySwitchingContainer(@Nonnull SerializableFunction<ID, Component> componentFactory) {
        addClassName("crud20-lazy-switching-container");
        this.componentFactory = componentFactory;
    }

    public final void switchTo(@Nullable ID id) {
        getContent().removeAll();
        this.selection = id;
        if (id != null) {
            getContent().add(componentCache.computeIfAbsent(id, componentFactory));
        }
    }

    public final @Nonnull Optional<ID> selection() {
        return Optional.ofNullable(selection);
    }

    public final void clear() {
        switchTo(null);
    }
}
