package org.vaadin.playground.crud20.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class ComponentUtil2 {

    // This class contains methods that we could consider adding to ComponentUtil (or why not to Component?)

    private static final Logger log = LoggerFactory.getLogger(ComponentUtil2.class);

    private ComponentUtil2() {
    }

    public static void registerOnAttach(@Nonnull Component owner, @Nonnull Supplier<Registration> registerAction) {
        if (owner.isAttached()) {
            register(owner, registerAction);
        } else {
            owner.addAttachListener(attachEvent -> register(owner, registerAction));
        }
    }

    private static void register(@Nonnull Component owner, @Nonnull Supplier<Registration> registerAction) {
        var registration = registerAction.get();
        log.trace("Added registration {} to {}", registration, owner);
        unregisterOnDetach(owner, registration);
    }

    private static void unregisterOnDetach(@Nonnull Component owner, @Nonnull Registration registration) {
        owner.addDetachListener(detachEvent -> {
            registration.remove();
            log.trace("Removed registration {} from {}", registration, owner);
        });
    }
}
