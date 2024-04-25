package org.vaadin.playground.crud20.demo.views.teams;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.playground.crud20.demo.views.MainLayout;

@Route(value = "teams", layout = MainLayout.class)
public class TeamsView extends VerticalLayout {

    public TeamsView() {
        add("This is the teams view");
    }
}
