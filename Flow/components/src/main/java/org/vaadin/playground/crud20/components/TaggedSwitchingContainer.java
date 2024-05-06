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

public class TaggedSwitchingContainer<TAG> extends Composite<Div> implements HasSize {

    private final Map<TAG, Component> componentCache = new HashMap<>();
    private final SerializableFunction<TAG, Component> componentFactory;
    private TAG selection;

    public TaggedSwitchingContainer(@Nonnull SerializableFunction<TAG, Component> componentFactory) {
        addClassName("crud20-lazy-switching-container");
        this.componentFactory = componentFactory;
    }

    public final void switchTo(@Nullable TAG tag) {
        getContent().removeAll();
        this.selection = tag;
        if (tag != null) {
            getContent().add(componentCache.computeIfAbsent(tag, componentFactory));
        }
    }

    public final @Nonnull Optional<TAG> selection() {
        return Optional.ofNullable(selection);
    }
}
