package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.shared.HasThemeVariant;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@CssImport("./crud20-styles/toolbar.css")
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

    public void setTitle(@Nullable Component title) {
        if (this.title != null) {
            this.title.removeFromParent();
        }
        this.title = title;
        if (title != null) {
            title.addClassName("crud20-toolbar-title");
            getContent().addComponentAsFirst(title);
        }
    }

    public void addToStart(@Nonnull Component... components) {
        start.add(components);
    }

    public void addToMiddle(@Nonnull Component... components) {
        middle.add(components);
    }

    public void addToEnd(@Nonnull Component... components) {
        end.add(components);
    }
}
