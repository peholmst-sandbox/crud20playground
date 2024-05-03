package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.shared.HasThemeVariant;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class Toolbar extends Composite<Div> implements HasThemeVariant<ToolbarVariant>, HasSize {

    private Component title;
    private final Div start;
    private final Div middle;
    private final Div end;

    public Toolbar() {
        addClassName("crud20-toolbar");

        Div sections = new Div();
        sections.addClassName("crud20-toolbar-section-container");
        getContent().add(sections);

        start = createSection();
        middle = createSection();
        end = createSection();

        sections.add(start);
        sections.add(middle);
        sections.add(end);
    }

    private Div createSection() {
        var section = new Div();
        section.addClassName("crud20-toolbar-section");
        return section;
    }

    public final void setTitle(@Nullable Component title) {
        if (this.title != null) {
            this.title.removeFromParent();
        }
        this.title = title;
        if (title != null) {
            title.addClassName("crud20-toolbar-title");
            getContent().addComponentAsFirst(title);
        }
    }

    public final void setTitleText(@Nullable String titleText) {
        if (titleText == null) {
            setTitle(null);
        } else {
            setTitle(new Span(titleText));
        }
    }

    public final void addToStart(@Nonnull Component... components) {
        start.add(components);
    }

    public final void addToMiddle(@Nonnull Component... components) {
        middle.add(components);
    }

    public final void addToEnd(@Nonnull Component... components) {
        end.add(components);
    }
}
