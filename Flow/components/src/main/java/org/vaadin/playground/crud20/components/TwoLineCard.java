package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.shared.HasThemeVariant;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class TwoLineCard extends Composite<Div> implements HasThemeVariant<TwoLineCardVariant>, HasSize {

    private Component avatar;
    private final Div primaryLine;
    private final Div secondaryLine;

    public TwoLineCard() {
        addClassName("crud20-two-line-card");
        var details = new Div();
        details.addClassName("crud20-two-line-card-details");
        getContent().add(details);
        primaryLine = new Div();
        primaryLine.addClassName("crud20-two-line-card-primary-line");
        secondaryLine = new Div();
        secondaryLine.addClassName("crud20-two-line-card-secondary-line");
        details.add(primaryLine, secondaryLine);
    }

    public final void setAvatar(@Nullable Component avatar) {
        if (this.avatar != null) {
            this.avatar.removeFromParent();
        }
        this.avatar = avatar;
        if (avatar != null) {
            avatar.addClassName("crud20-two-line-card-avatar");
            getContent().addComponentAsFirst(avatar);
        }
    }

    public final void setAvatar(@Nullable String name, @Nullable String url) {
        if (name == null && url == null) {
            setAvatar(null);
        } else {
            setAvatar(new Avatar(name, url));
        }
    }

    public final void setPrimaryLine(@Nullable Component primaryLine) {
        this.primaryLine.removeAll();
        if (primaryLine != null) {
            this.primaryLine.add(primaryLine);
        }
    }

    public final void addToPrimaryLine(@Nonnull Component... components) {
        this.primaryLine.add(components);
    }

    public final void setPrimaryLineText(@Nullable String primaryLineText) {
        if (primaryLineText == null) {
            setPrimaryLine(null);
        } else {
            setPrimaryLine(new Span(primaryLineText));
        }
    }

    public final void setSecondaryLine(@Nullable Component secondaryLine) {
        this.secondaryLine.removeAll();
        if (secondaryLine != null) {
            this.secondaryLine.add(secondaryLine);
        }
    }

    public final void setSecondaryLineText(@Nullable String secondaryLineText) {
        if (secondaryLineText == null) {
            setSecondaryLine(null);
        } else {
            setSecondaryLine(new Span(secondaryLineText));
        }
    }

    public final void addToSecondaryLine(@Nonnull Component... components) {
        this.secondaryLine.add(components);
    }
}
