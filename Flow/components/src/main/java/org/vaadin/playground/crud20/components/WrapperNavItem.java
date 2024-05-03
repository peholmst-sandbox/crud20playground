package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.HighlightCondition;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class WrapperNavItem extends Composite<RouterLink> {

    public WrapperNavItem(@Nonnull Component item,
                          @Nonnull Class<? extends Component> navigationTarget,
                          @Nullable RouteParameters routeParameters) {
        addClassName("crud20-wrapper-nav-item");
        getContent().add(item);
        getContent().setRoute(navigationTarget, routeParameters);
        getContent().setHighlightCondition(HighlightConditions.never());
    }

    public void setHighlightCondition(@Nullable HighlightCondition<WrapperNavItem> highlightCondition) {
        if (highlightCondition == null) {
            getContent().setHighlightCondition(HighlightConditions.never());
        } else {
            getContent().setHighlightCondition((c, event) -> highlightCondition.shouldHighlight(this, event));
        }
    }

    public void setHighlighted(boolean highlighted) {
        getContent().getHighlightAction().highlight(getContent(), highlighted);
    }
}
