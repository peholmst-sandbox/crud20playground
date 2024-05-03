package org.vaadin.playground.crud20.demo.views.employees;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.playground.crud20.components.*;
import org.vaadin.playground.crud20.data.selection.SelectionModel;
import org.vaadin.playground.crud20.data.selection.SingleSelectionModel;
import org.vaadin.playground.crud20.demo.sampledata.Employee;
import org.vaadin.playground.crud20.demo.sampledata.EmployeeId;
import org.vaadin.playground.crud20.demo.views.MainLayout;
import org.vaadin.playground.crud20.router.selection.SingleSelectionNavigationController;

@Route(value = "employees/:employeeId?/:tab?", layout = MainLayout.class)
public class EmployeesView extends MasterDetailLayout implements BeforeEnterObserver {

    private final SingleSelectionModel<EmployeeId> selectionModel = SelectionModel.single();
    private final SingleSelectionNavigationController<EmployeeId> selectionController = new SingleSelectionNavigationController<>(
            this,
            selectionModel,
            "employeeId",
            EmployeeId::new
    );

    public EmployeesView() {
        setHeightFull();

        var master = new ContentContainer();
        master.addThemeVariants(ContentContainerVariant.HEADER_BORDER, ContentContainerVariant.HEADER_PADDING);
        master.addClassName(LumoUtility.Background.CONTRAST_5);

        var addEmployee = new Button("Add New");
        addEmployee.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var masterToolbar = new Toolbar();
        masterToolbar.setTitleText("Employees");
        masterToolbar.addToEnd(addEmployee);
        master.addToHeader(masterToolbar);

        var search = new TextField();
        search.setPlaceholder("Search");
        master.addToHeader(search);

        var employeesList = new VirtualList<Employee>();
        employeesList.setHeightFull();
        employeesList.setWidthFull();
        employeesList.setItems(Employee.createEmployees());
        employeesList.setRenderer(new ComponentRenderer<>(
                employee -> {
                    var employeeItem = new WrapperNavItem(
                            new EmployeeCard(employee),
                            EmployeesView.class,
                            selectionController.getRouteParametersForSelection(employee.id())
                    );
                    // TODO Would it be possible to combine these two lines into a single one?
                    employeeItem.setHighlightCondition(selectionController.getHighlightConditionForSelection(employee.id()));
                    employeeItem.setHighlighted(selectionController.isSelected(employee.id()));
                    employeeItem.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM);
                    return employeeItem;
                }
        ));
        master.addContent(employeesList);
        master.setWidth("300px");
        setMaster(master);

        selectionModel.addAction(this::showEmployee, this::showNoSelection);
    }

    private void showEmployee(EmployeeId employeeId) {
        setDetail(new EmployeeDetails(selectionController));
    }

    private void showNoSelection() {
        var noSelection = new Span("Please select an employee");
        noSelection.addClassName(LumoUtility.Padding.MEDIUM);
        setDetail(noSelection);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        selectionController.handleBeforeEnterEvent(event);
        // TODO If the virtual list contains many items, how do we scroll to the selected item? We only have the ID.
    }
}