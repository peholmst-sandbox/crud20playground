package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.shared.ThemeVariant;

public enum ToolbarVariant implements ThemeVariant {
    DARK("dark");

    private final String variant;

    ToolbarVariant(String variant) {
        this.variant = variant;
    }

    @Override
    public String getVariantName() {
        return variant;
    }
}
