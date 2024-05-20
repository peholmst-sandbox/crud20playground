package org.vaadin.playground.crud20.data.property.action;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableRunnable;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;

public final class ActionUtils {

    private ActionUtils() {
    }

    public static <T> void doIfPresent(@Nonnull Property<T> property, @Nonnull SerializableConsumer<T> action) {
        if (property.isPresent()) {
            action.accept(property.value());
        }
    }

    public static <T> void doIfPresentOrElse(@Nonnull Property<T> property, @Nonnull SerializableConsumer<T> action,
                                             @Nonnull SerializableRunnable emptyAction) {
        if (property.isPresent()) {
            action.accept(property.value());
        } else {
            emptyAction.run();
        }
    }
}
