package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TaggedTabs<TAG> extends Composite<Tabs> implements HasSize {

    private final Map<TAG, Tab> idToTab = new HashMap<>();

    public final void add(@Nonnull TAG tag, @Nonnull Tab tab) {
        if (idToTab.putIfAbsent(tag, tab) != null) {
            throw new IllegalArgumentException("Id has already been used");
        }
        getContent().add(tab);
    }

    public final void switchTo(@Nullable TAG tag) {
        if (tag == null) {
            getContent().setSelectedTab(null);
        } else {
            getContent().setSelectedTab(idToTab.get(tag));
        }
    }
}
