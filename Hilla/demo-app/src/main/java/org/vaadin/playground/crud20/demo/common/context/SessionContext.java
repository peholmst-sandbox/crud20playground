package org.vaadin.playground.crud20.demo.common.context;

import net.pkhapps.commons.domain.primitives.IpAddress;
import net.pkhapps.commons.domain.primitives.UserId;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

@Component
public class SessionContext {

    // TODO Implement me!

    private final Clock clock;

    public SessionContext(Clock clock) {
        this.clock = clock;
    }

    public @NotNull Optional<IpAddress> clientIpAddress() {
        return Optional.empty();
    }

    public @NotNull Optional<UserId> currentUser() {
        return Optional.empty();
    }

    public @NotNull OffsetDateTime now() {
        return OffsetDateTime.now(clock);
    }
}
