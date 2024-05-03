package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.dom.Element;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ContentContainer extends Composite<FlexLayout> implements HasThemeVariant<ContentContainerVariant>, HasSize {

    private final LazyComponent<Div> headerDiv = new LazyComponent<>(this::createHeaderDiv, getContent()::addComponentAsFirst);
    private final Div contentDiv;
    private final LazyComponent<Div> footerDiv = new LazyComponent<>(this::createFooterDiv, getContent()::add);

    public ContentContainer() {
        addClassName("crud2-content-container");
        contentDiv = new Div();
        contentDiv.addClassName("crud2-content-container-content");

        getContent().setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        getContent().add(contentDiv);
        getContent().setFlexGrow(1, contentDiv);
    }

    private Div createHeaderDiv() {
        var header = new Div();
        header.addClassName("crud2-content-container-header");
        return header;
    }

    private Div createFooterDiv() {
        var footer = new Div();
        footer.addClassName("crud2-content-container-footer");
        return footer;
    }

    public final void setHeader(@Nullable Component header) {
        headerDiv.get().removeAll();
        if (header != null) {
            headerDiv.get().add(header);
        }
    }

    public final void addToHeader(@Nonnull Component... components) {
        headerDiv.get().add(components);
    }

    public final void setContent(@Nullable Component content) {
        contentDiv.removeAll();
        if (content != null) {
            contentDiv.add(content);
        }
    }

    public final void setContentElement(@Nullable Element element) {
        contentDiv.removeAll();
        if (element != null) {
            contentDiv.getElement().appendChild(element);
        }
    }

    public final void addContent(@Nonnull Component... content) {
        contentDiv.add(content);
    }

    public final void removeContent(@Nonnull Component... content) {
        contentDiv.remove(content);
    }

    public final void setFooter(@Nullable Component footer) {
        footerDiv.get().removeAll();
        if (footer != null) {
            footerDiv.get().add(footer);
        }
    }

    public final void addToFooter(@Nonnull Component... components) {
        footerDiv.get().add(components);
    }
}
