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
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;
import org.vaadin.playground.crud20.components.*;
import org.vaadin.playground.crud20.demo.sampledata.Employee;
import org.vaadin.playground.crud20.demo.views.MainLayout;

import java.time.LocalDate;
import java.time.ZoneId;


@Route(value = "employees", layout = MainLayout.class)
public class EmployeesView extends MasterDetailLayout {

    public EmployeesView() {
        setHeightFull();

        /** Create content for the master **/
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

        VirtualList<Employee> employeesList = new VirtualList();
        employeesList.setHeightFull();
        employeesList.setWidthFull();
        employeesList.setItems(Employee.createEmployees());
        employeesList.setRenderer(employeeCardRenderer);
        master.addContent(employeesList);
        master.setWidth("300px");
        setMaster(master);

        /** Create content for the detail **/

        var detail = new ContentContainer();
        detail.addThemeVariants(ContentContainerVariant.FOOTER_BORDER, ContentContainerVariant.FOOTER_PADDING);

        var headerToolbar = new Toolbar();
        headerToolbar.addThemeVariants(ToolbarVariant.PADDING);
        detail.addToHeader(headerToolbar);

        var employeeDetails = new TwoLineCard();
        employeeDetails.setAvatar("Cody Fisher", "images/avatars/cody_fisher.jpg");
        employeeDetails.setPrimaryLineText("Cody Fisher");
        employeeDetails.setSecondaryLineText("Scrum Master");
        employeeDetails.addThemeVariants(TwoLineCardVariant.XLARGE);
        headerToolbar.setTitle(employeeDetails);

        var tabs = new Tabs();
        tabs.setWidthFull();
        Tab personal = new Tab("Personal");
        Tab job = new Tab("Job");
        Tab emergency = new Tab("Emergency");
        Tab documents = new Tab("Documents");
        tabs.add(personal, job, emergency, documents);
        detail.addToHeader(tabs);

        /* Set the content for detail pane*/
        /* TODO Add other tabs and handling switching */
        detail.setContent(buildPersonalInfo());

        var footerToolbar = new Toolbar();
        var edit = new Button("Edit");
        var share = new Button("Share");
        var delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        footerToolbar.addToStart(edit, share, delete);
        detail.setFooter(footerToolbar);

        setDetail(detail);

        VerticalLayout info = new VerticalLayout();
        VerticalLayout mainDetail = new VerticalLayout();
    }

    private HorizontalLayout buildPersonalInfo() {

        HorizontalLayout personalInfo = new HorizontalLayout();
        personalInfo.setSpacing(false);
        personalInfo.setSizeFull();

        /* Create the personal summary*/
        ContentContainer summary = new ContentContainer();
        VerticalLayout summaryContent = new VerticalLayout();
        summaryContent.addClassName(LumoUtility.Gap.SMALL);
        summaryContent.setSpacing(false);
        summaryContent.setWidth(300, Unit.PIXELS);
        H4 summaryTitle = new H4("Summary");
        summaryTitle.addClassName(LumoUtility.Margin.Bottom.LARGE);
        H4 managerTitle= new H4("Manager");
        var manager = createEmployeeItem("Esther Howard", "Software Development Manager", "images/avatars/esther_howard.jpg");
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
        TextField firstName = new TextField("First name", "Cody","");
        TextField middleName = new TextField("Middle Name", "", "");
        TextField lastName = new TextField("Last name","Fisher", "");
        TextField preferredName = new TextField("Preferred Name","Cody", "");
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
        TextArea streetAddress = new TextArea("Street Address","1234 Maple Street, Apartment 5A, Oakville, Meadowbrook County, Anytown, USA", "");
        TextField city = new TextField("City", "Anytown","");
        TextField province = new TextField("Province", "Meadowbrook County","");
        TextField postalCode = new TextField("Postal Code", "12345","");
        TextField country = new TextField("Country", "USA","");
        Hr homeAddressDivider = createDivider();
        personalInfoForm.add(homeAddress, streetAddress, city, province, postalCode, country, homeAddressDivider);
        personalInfoForm.setColspan(homeAddress, 4);
        personalInfoForm.setColspan(streetAddress, 4);
        personalInfoForm.setColspan(homeAddressDivider, 4);

        /* Contact */
        H4 contact = new H4("Contact");
        TextField workPhone = new TextField("Work Phone","+358 12 345 6789", "");
        TextField mobilePhone = new TextField("Mobile Phone", "+358 12 345 6789","");
        TextField homePhone = new TextField("Home Phone", "-","");
        TextField workEmail = new TextField("Work Email", "codyfisher@abccompany.com","");
        TextField slack = new TextField("Slack", "codyfisher","");
        TextField github = new TextField("Github", "codyfisher","");
        Hr contactDivider = createDivider();
        personalInfoForm.add(contact, workPhone, mobilePhone, homePhone, workEmail, slack, github);
        personalInfoForm.setColspan(contact, 4);
        personalInfoForm.setColspan(slack, 4);
        personalInfoForm.setColspan(github, 4);
        personalInfoForm.setColspan(contactDivider, 4);

        /* Make everything readonly */
        setFormFieldsReadOnly(personalInfoForm);

        formWrapper.setContent(personalInfoForm);

        personalInfo.add(summary, formWrapper);

        return personalInfo;
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
                    if(component instanceof TextFieldBase) {
                        ((TextFieldBase) component).setReadOnly(true);
                        ((HasTheme) component).addThemeName("read-only-text-style");
                    }

                    else if(component instanceof AbstractSinglePropertyField) {
                        ((AbstractSinglePropertyField) component).setReadOnly(true);
                        ((HasTheme) component).addThemeName("read-only-text-style");
                    }

                }
        );
    }

    private TwoLineCard createEmployeeItem(String name, String role, String avatarImageUrl) {
        var employeeItem = new TwoLineCard();
        employeeItem.setAvatar(name, avatarImageUrl);
        employeeItem.setPrimaryLineText(name);
        employeeItem.setSecondaryLineText(role);
        return employeeItem;
    }
    private ComponentRenderer<Component, Employee> employeeCardRenderer = new ComponentRenderer<>(
            employee -> {
                var employeeItem = createEmployeeItem(employee.getName(), employee.getRole(), "images/avatars/" + employee.getProfilePicUrl());
                employeeItem.addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.Padding.Horizontal.MEDIUM);
                return employeeItem;
            }
    );


}