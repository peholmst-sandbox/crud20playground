package org.vaadin.playground.crud20.demo.views;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.RouterLayout;
import org.vaadin.playground.crud20.components.*;
import org.vaadin.playground.crud20.demo.views.employees.EmployeesView;
import org.vaadin.playground.crud20.demo.views.locations.LocationsView;
import org.vaadin.playground.crud20.demo.views.teams.TeamsView;

public class MainLayout extends ContentContainer implements RouterLayout {

    public MainLayout() {
        var navigationTabs = new TopNav();
        navigationTabs.addItem(
                new TopNavItem("Employees", EmployeesView.class),
                new TopNavItem("Teams", TeamsView.class),
                new TopNavItem("Locations", LocationsView.class)
        );
        navigationTabs.setHeightFull();

        var userAvatar = new Avatar("FL");
        userAvatar.setColorIndex(1);

        var logo = new Image("images/vaadin.png", "Vaadin");
        logo.setHeight("44px");

        var header = new Toolbar();
        header.addThemeVariants(ToolbarVariant.DARK);
        header.setTitle(logo);
        header.addToStart(navigationTabs);
        header.addToEnd(userAvatar);
        header.setHeight("50px");

        setHeader(header);
        setSizeFull();
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        setContentElement(content.getElement());
    }
}
