package org.vaadin.playground.crud20.demo.views.locations;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.playground.crud20.demo.views.MainLayout;

@Route(value = "locations", layout = MainLayout.class)
public class LocationsView extends VerticalLayout {

    public LocationsView() {
        add("This is the locations view");
    }
}
