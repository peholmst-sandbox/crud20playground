package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.shared.ThemeVariant;

public enum ContentContainerVariant implements ThemeVariant {

    HEADER_BORDER("header-border"), HEADER_PADDING("header-padding");

    private ContentContainerVariant(String variant) {
        this.variant = variant;
    }

    private final String variant;

    @Override
    public String getVariantName() {
        return variant;
    }
}
