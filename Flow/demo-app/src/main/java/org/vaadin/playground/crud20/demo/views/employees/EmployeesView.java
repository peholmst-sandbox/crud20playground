package org.vaadin.playground.crud20.demo.views.employees;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.components.*;
import org.vaadin.playground.crud20.data.property.WritableProperty;
import org.vaadin.playground.crud20.data.property.action.TriggerUtils;
import org.vaadin.playground.crud20.demo.sampledata.Employee;
import org.vaadin.playground.crud20.demo.sampledata.EmployeeId;
import org.vaadin.playground.crud20.demo.sampledata.EmployeeService;
import org.vaadin.playground.crud20.demo.views.MainLayout;
import org.vaadin.playground.crud20.router.param.HighlightConditions2;
import org.vaadin.playground.crud20.router.param.RouteParamDefinition;
import org.vaadin.playground.crud20.router.param.RouteParameterBinder;

@Route(value = "employees/:employeeId?/:tab?", layout = MainLayout.class)
public class EmployeesView extends MasterDetailLayout implements BeforeEnterObserver {

    // The idea here is that you should be able to use a nice API to navigate to and within this view, having the
    // possibility to use domain primitives instead of just strings, integers and longs. Because the view defines
    // the parameter template, it makes sense for the view to define the navigation API as well.

    public enum Tab {
        personal, job, emergency, documents
    }

    public static final RouteParamDefinition<EmployeeId> EMPLOYEE_ID = RouteParamDefinition.forObject("employeeId", EmployeeId::new, EmployeeId::toString);
    public static final RouteParamDefinition<Tab> TAB = RouteParamDefinition.forObject("tab", Tab::valueOf, Tab::name);

    public static void navigateTo(@Nonnull EmployeeId employeeId) {
        UI.getCurrent().navigate(EmployeesView.class, routeParamsForEmployee(employeeId));
    }

    public static void navigateTo(@Nonnull EmployeeId employeeId, @Nonnull Tab tab) {
        UI.getCurrent().navigate(EmployeesView.class, routeParamsForEmployee(employeeId, tab));
    }

    public static @Nonnull RouteParameters routeParamsForEmployee(@Nonnull EmployeeId employeeId) {
        return routeParamsForEmployee(employeeId, Tab.personal);
    }

    public static @Nonnull RouteParameters routeParamsForEmployee(@Nonnull EmployeeId employeeId, @Nonnull Tab tab) {
        return new RouteParameters(EMPLOYEE_ID.toRouteParam(employeeId), TAB.toRouteParam(tab));
    }

    private final RouteParameterBinder routeParameterBinder = new RouteParameterBinder();
    private final WritableProperty<EmployeeId> selectedEmployee = WritableProperty.create();
    private final WritableProperty<Tab> selectedTab = WritableProperty.create();

    public EmployeesView(EmployeeService employeeService) {
        routeParameterBinder.bindSignalToParameter(selectedEmployee, EMPLOYEE_ID);
        routeParameterBinder.bindSignalToParameter(selectedTab, TAB);

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
        employeesList.setItems(employeeService.findAll()); // TODO Use pagination and a data provider instead
        employeesList.setRenderer(new ComponentRenderer<>(
                employee -> {
                    var employeeItem = new WrapperNavItem(
                            new EmployeeCard(employee),
                            EmployeesView.class,
                            routeParamsForEmployee(employee.id())
                    );
                    employeeItem.setHighlightCondition(HighlightConditions2.paramEquals(EMPLOYEE_ID, employee.id()));
                    // This next line is needed if the ID has been set manually in the browser (e.g. by clicking a link)
                    // I would love to get rid of it and have the highlight condition handle everything.
                    employeeItem.setHighlighted(selectedEmployee.contains(employee.id()));
                    employeeItem.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM);
                    return employeeItem;
                }
        ));
        master.addContent(employeesList);
        master.setWidth("300px");
        setMaster(master);
        TriggerUtils.addTriggerWhenPresentOrElse(selectedEmployee.mapOptional(employeeService::findById), this::showEmployee, this::showNoSelection); // TODO Is there a need to save this registration somewhere to prevent premature GC?
    }

    private void showEmployee(@Nonnull Employee employee) {
        setDetail(new EmployeeDetails(employee, selectedTab));
    }

    private void showNoSelection() {
        var noSelection = new Span("Please select an employee");
        noSelection.addClassName(LumoUtility.Padding.MEDIUM);
        setDetail(noSelection);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        routeParameterBinder.load(event.getRouteParameters());
        // TODO If the virtual list contains many items, how do we scroll to the selected item? We only have the ID.
    }
}