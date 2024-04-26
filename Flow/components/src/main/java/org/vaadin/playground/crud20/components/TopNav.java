package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nonnull;

public class TopNav extends Composite<Div> implements HasSize {

    public TopNav() {
        addClassName("crud20-top-nav");
    }

    public void addItem(@Nonnull TopNavItem... items) {
        getContent().add(items);
    }
}
