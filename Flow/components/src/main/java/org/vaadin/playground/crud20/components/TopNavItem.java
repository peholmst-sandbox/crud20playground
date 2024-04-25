package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.Nonnull;

public class TopNavItem extends Composite<RouterLink> {

    public TopNavItem(@Nonnull String text, @Nonnull Class<? extends Component> navigationTarget) {
        addClassName("crud20-top-nav-item");
        getContent().setText(text);
        getContent().setRoute(navigationTarget);
    }
}
