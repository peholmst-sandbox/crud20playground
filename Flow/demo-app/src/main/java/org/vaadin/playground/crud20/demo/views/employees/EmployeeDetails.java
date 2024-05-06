package org.vaadin.playground.crud20.demo.views.employees;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;
import org.vaadin.playground.crud20.components.*;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.demo.sampledata.Employee;
import org.vaadin.playground.crud20.demo.sampledata.EmployeeId;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.vaadin.playground.crud20.demo.views.employees.EmployeesView.routeParamsForEmployee;
import static org.vaadin.playground.crud20.util.ComponentUtil2.registerOnAttach;

class EmployeeDetails extends ContentContainer {

    private final Employee employee;

    public EmployeeDetails(Employee employee, Property<EmployeesView.Tab> selectedTab) {
        this.employee = employee;
        addThemeVariants(ContentContainerVariant.FOOTER_BORDER, ContentContainerVariant.FOOTER_PADDING);

        var headerToolbar = new Toolbar();
        headerToolbar.addThemeVariants(ToolbarVariant.PADDING);
        addToHeader(headerToolbar);

        var headerEmployeeInfo = new TwoLineCard();
        headerEmployeeInfo.setAvatar("Cody Fisher", "images/avatars/cody_fisher.jpg");
        headerEmployeeInfo.setPrimaryLineText("Cody Fisher");
        headerEmployeeInfo.setSecondaryLineText("Scrum Master");
        headerEmployeeInfo.addThemeVariants(TwoLineCardVariant.XLARGE);
        headerToolbar.setTitle(headerEmployeeInfo);

        var tabs = new TaggedTabs<EmployeesView.Tab>();
        tabs.setWidthFull();
        tabs.add(EmployeesView.Tab.personal, createTab("Personal", EmployeesView.Tab.personal));
        tabs.add(EmployeesView.Tab.job, createTab("Job", EmployeesView.Tab.job));
        tabs.add(EmployeesView.Tab.emergency, createTab("Emergency", EmployeesView.Tab.emergency));
        tabs.add(EmployeesView.Tab.documents, createTab("Documents", EmployeesView.Tab.documents));
        addToHeader(tabs);

        var tabContainer = new TaggedSwitchingContainer<>(this::createTabContent);
        tabContainer.setSizeFull();
        setContent(tabContainer);

        var footerToolbar = new Toolbar();
        var edit = new Button("Edit");
        var share = new Button("Share");
        var delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        footerToolbar.addToStart(edit, share, delete);
        setFooter(footerToolbar);

        registerOnAttach(this, () -> selectedTab.triggerIfPresentOrElse(tabContainer::switchTo, () -> tabContainer.switchTo(EmployeesView.Tab.personal)));
        registerOnAttach(this, () -> selectedTab.triggerIfPresentOrElse(tabs::switchTo, () -> tabs.switchTo(EmployeesView.Tab.personal)));
    }

    private Tab createTab(String text, EmployeesView.Tab tab) {
        // This is the only way of navigating between tabs: through the router. Another alternative would be to have
        // the UI interact with the property, and then have a two-way binding between the property and the router.
        var link = new RouterLink(text, EmployeesView.class, routeParamsForEmployee(employee.id(), tab));
        return new Tab(link);
    }

    private Component createTabContent(EmployeesView.Tab tab) {
        return switch (tab) {
            case personal -> new PersonalTab();
            case job -> new JobTab();
            case emergency -> new EmergencyTab();
            case documents -> new DocumentsTab();
        };
    }

    public class PersonalTab extends HorizontalLayout {
        public PersonalTab() {
            setSpacing(false);
            setSizeFull();

            /* Create the personal summary*/
            ContentContainer summary = new ContentContainer();
            VerticalLayout summaryContent = new VerticalLayout();
            summaryContent.addClassName(LumoUtility.Gap.SMALL);
            summaryContent.setSpacing(false);
            summaryContent.setWidth(300, Unit.PIXELS);
            H4 summaryTitle = new H4("Summary");
            summaryTitle.addClassName(LumoUtility.Margin.Bottom.LARGE);
            H4 managerTitle = new H4("Manager");
            var manager = new WrapperNavItem(new EmployeeCard(new Employee(new EmployeeId(1), "Esther Howard", "Software Development Manager", "esther_howard.jpg")),
                    EmployeesView.class,
                    routeParamsForEmployee(new EmployeeId(1))
            );
            summaryContent.add(
                    summaryTitle,
                    createTextItem(LineAwesomeIcon.PHONE_SOLID, "+358 12 345 6789"),
                    createTextItem(LineAwesomeIcon.ENVELOPE, "codyfisher@abccompany.com"),
                    createDivider(),
                    createTextItem(LineAwesomeIcon.CALENDAR, "Jul 1, 2020"),
                    createTextItem(LineAwesomeIcon.USER_CLOCK_SOLID, "3 years, 9 months and 14 days"),
                    createDivider(),
                    createTextItem(LineAwesomeIcon.BRIEFCASE_SOLID, "Full-Time"),
                    createTextItem(LineAwesomeIcon.USER_CIRCLE_SOLID, "Research and Development (R&D)"),
                    createTextItem(LineAwesomeIcon.CLOCK_SOLID, "6:32 PM local time"),
                    createDivider(),
                    managerTitle,
                    manager
            );
            summary.setContent(summaryContent);

            ContentContainer formWrapper = new ContentContainer();
            formWrapper.addClassName(LumoUtility.Border.LEFT);

            FormLayout personalInfoForm = new FormLayout();
            personalInfoForm.addClassNames(LumoUtility.Padding.MEDIUM);

            personalInfoForm.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("500px", 2),
                    new FormLayout.ResponsiveStep("800px", 4)
            );
            /* Basic Info */
            H4 basicInfo = new H4("Basic Information");
            TextField firstName = new TextField("First name", "Cody", "");
            TextField middleName = new TextField("Middle Name", "", "");
            TextField lastName = new TextField("Last name", "Fisher", "");
            TextField preferredName = new TextField("Preferred Name", "Cody", "");
            DatePicker birthDate = new DatePicker("Birth Date", LocalDate.now(ZoneId.systemDefault()));
            ComboBox gender = new ComboBox<>("Gender");
            gender.setItems("Male", "Female");
            gender.setValue("Male");
            TextArea dietaryNotes = new TextArea("Dietary Notes", "Embracing a flexible approach to eating, with a focus on plant-based meals while occasionally enjoying meat and seafood. No known allergies.", "");
            Hr basicInfoDivider = createDivider();
            personalInfoForm.add(basicInfo, firstName, middleName, lastName, preferredName, birthDate, gender, dietaryNotes, basicInfoDivider);
            personalInfoForm.setColspan(basicInfo, 4);
            personalInfoForm.setColspan(dietaryNotes, 4);
            personalInfoForm.setColspan(basicInfoDivider, 4);

            /* Home Address */
            H4 homeAddress = new H4("Home Address");
            TextArea streetAddress = new TextArea("Street Address", "1234 Maple Street, Apartment 5A, Oakville, Meadowbrook County, Anytown, USA", "");
            TextField city = new TextField("City", "Anytown", "");
            TextField province = new TextField("Province", "Meadowbrook County", "");
            TextField postalCode = new TextField("Postal Code", "12345", "");
            TextField country = new TextField("Country", "USA", "");
            Hr homeAddressDivider = createDivider();
            personalInfoForm.add(homeAddress, streetAddress, city, province, postalCode, country, homeAddressDivider);
            personalInfoForm.setColspan(homeAddress, 4);
            personalInfoForm.setColspan(streetAddress, 4);
            personalInfoForm.setColspan(homeAddressDivider, 4);

            /* Contact */
            H4 contact = new H4("Contact");
            TextField workPhone = new TextField("Work Phone", "+358 12 345 6789", "");
            TextField mobilePhone = new TextField("Mobile Phone", "+358 12 345 6789", "");
            TextField homePhone = new TextField("Home Phone", "-", "");
            TextField workEmail = new TextField("Work Email", "codyfisher@abccompany.com", "");
            TextField slack = new TextField("Slack", "codyfisher", "");
            TextField github = new TextField("Github", "codyfisher", "");
            Hr contactDivider = createDivider();
            personalInfoForm.add(contact, workPhone, mobilePhone, homePhone, workEmail, slack, github);
            personalInfoForm.setColspan(contact, 4);
            personalInfoForm.setColspan(slack, 4);
            personalInfoForm.setColspan(github, 4);
            personalInfoForm.setColspan(contactDivider, 4);

            /* Make everything readonly */
            setFormFieldsReadOnly(personalInfoForm);

            formWrapper.setContent(personalInfoForm);

            add(summary, formWrapper);

        }

        private Hr createDivider() {
            Hr divider = new Hr();
            divider.addClassName(LumoUtility.Margin.Vertical.MEDIUM);
            return divider;
        }

        private HorizontalLayout createTextItem(LineAwesomeIcon icon, String text) {
            HorizontalLayout textItem = new HorizontalLayout();
            textItem.setSpacing(false);
            textItem.addClassNames(LumoUtility.Gap.SMALL);
            Span textComponent = new Span(text);
            textComponent.addClassNames(LumoUtility.TextColor.TERTIARY, LumoUtility.FontSize.SMALL);
            SvgIcon iconComponent = icon.create();
            textItem.add(iconComponent, textComponent);
            return textItem;
        }

        /* Make form fields look like text */
        private void setFormFieldsReadOnly(Component form) {
            form.getChildren().forEach(
                    component -> {
                        if (component instanceof TextFieldBase) {
                            ((TextFieldBase) component).setReadOnly(true);
                            ((HasTheme) component).addThemeName("read-only-text-style");
                        } else if (component instanceof AbstractSinglePropertyField) {
                            ((AbstractSinglePropertyField) component).setReadOnly(true);
                            ((HasTheme) component).addThemeName("read-only-text-style");
                        }

                    }
            );
        }
    }

    public class JobTab extends HorizontalLayout {
        public JobTab() {
            add("Job");
        }
    }

    public class EmergencyTab extends HorizontalLayout {
        public EmergencyTab() {
            add("Emergency");
        }
    }

    public class DocumentsTab extends HorizontalLayout {
        public DocumentsTab() {
            add("Documents");
        }
    }
}
