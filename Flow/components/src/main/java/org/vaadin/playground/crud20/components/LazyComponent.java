package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import jakarta.annotation.Nonnull;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

final class LazyComponent<C extends Component> implements Serializable {

    private C component;

    private final SerializableSupplier<C> componentFactory;
    private final SerializableConsumer<C> componentAttacher;

    public LazyComponent(@Nonnull SerializableSupplier<C> componentFactory, @Nonnull SerializableConsumer<C> componentAttacher) {
        this.componentFactory = requireNonNull(componentFactory);
        this.componentAttacher = requireNonNull(componentAttacher);
    }

    public @Nonnull C get() {
        if (component == null) {
            component = componentFactory.get();
            componentAttacher.accept(component);
        }
        return component;
    }
}
