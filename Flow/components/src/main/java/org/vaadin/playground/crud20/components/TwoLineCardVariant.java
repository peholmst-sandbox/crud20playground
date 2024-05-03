package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.shared.ThemeVariant;

public enum TwoLineCardVariant implements ThemeVariant {
    LARGE, XLARGE;

    @Override
    public String getVariantName() {
        return name().toLowerCase();
    }
}
