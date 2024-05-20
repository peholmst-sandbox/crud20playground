package org.vaadin.playground.crud20.data.property.action;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;

public final class TriggerUtils {

    private TriggerUtils() {
    }

    public static <T> @Nonnull Registration addTriggerWhenPresent(@Nonnull Property<T> property, @Nonnull SerializableConsumer<T> trigger) {
        return addTriggerWhenPresent(property, trigger, true);
    }

    public static <T> @Nonnull Registration addTriggerWhenPresent(@Nonnull Property<T> property, @Nonnull SerializableConsumer<T> trigger, boolean triggerNow) {
        if (triggerNow) {
            ActionUtils.doIfPresent(property, trigger);
        }
        return property.addListener(event -> {
            if (event.isPresent()) {
                trigger.accept(event.value());
            }
        });
    }

    public static <T> @Nonnull Registration addTriggerWhenPresentOrElse(@Nonnull Property<T> property, @Nonnull SerializableConsumer<T> trigger, @Nonnull SerializableRunnable emptyAction) {
        return addTriggerWhenPresentOrElse(property, trigger, emptyAction, true);
    }

    public static <T> @Nonnull Registration addTriggerWhenPresentOrElse(@Nonnull Property<T> property, @Nonnull SerializableConsumer<T> trigger, @Nonnull SerializableRunnable emptyAction, boolean triggerNow) {
        if (triggerNow) {
            ActionUtils.doIfPresentOrElse(property, trigger, emptyAction);
        }
        return property.addListener(event -> {
            if (event.isPresent()) {
                trigger.accept(event.value());
            } else {
                emptyAction.run();
            }
        });
    }
}
