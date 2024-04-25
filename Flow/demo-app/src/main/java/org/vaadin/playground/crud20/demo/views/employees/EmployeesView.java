package org.vaadin.playground.crud20.demo.views.employees;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.playground.crud20.demo.views.MainLayout;

@Route(value = "employees", layout = MainLayout.class)
public class EmployeesView extends VerticalLayout {

    public EmployeesView() {
        add("This is the employees view");
    }
}
