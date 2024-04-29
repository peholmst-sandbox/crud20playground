package org.vaadin.playground.crud20.demo.views.employees;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
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
import org.vaadin.playground.crud20.components.ContentContainer;
import org.vaadin.playground.crud20.components.MasterDetailLayout;
import org.vaadin.playground.crud20.demo.views.MainLayout;
import org.vaadin.playground.crud20.sampledata.Employee;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;
import java.time.ZoneId;


@Route(value = "employees", layout = MainLayout.class)
public class EmployeesView extends MasterDetailLayout {

    public EmployeesView() {
        setHeightFull();

        /** Create content for the master **/
        ContentContainer master = new ContentContainer();
        master.addClassName(LumoUtility.Background.CONTRAST_5);

        TextField search = new TextField();
        search.setWidthFull();
        search.setPlaceholder("Search");
        H3 masterTitle = new H3("Employees");
        Button addEmployee = new Button("Add New");
        addEmployee.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout titleContainer = new HorizontalLayout(masterTitle, addEmployee);
        titleContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);
        titleContainer.setAlignItems(Alignment.CENTER);
        titleContainer.setWidthFull();
        VerticalLayout masterHeader = new VerticalLayout(titleContainer, search);
        masterHeader.addClassNames(LumoUtility.Padding.Vertical.SMALL, LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Border.BOTTOM, LumoUtility.Gap.XSMALL);
        masterHeader.setWidthFull();
        master.setHeader(masterHeader);

        VirtualList<Employee> employeesList = new VirtualList();
        employeesList.setHeightFull();
        employeesList.setWidthFull();
        employeesList.setItems(Employee.createEmployees());
        employeesList.setRenderer(employeeCardRenderer);
        master.addContent(employeesList);
        master.setWidth("300px");
        setMaster(master);

        /** Create content for the detail **/

        /* Create a container for the detail's header, content and footer */
        ContentContainer detail = new ContentContainer();
        VerticalLayout detailHeader = new VerticalLayout();
        detailHeader.setPadding(false);
        detailHeader.setSpacing(false);
        HorizontalLayout employeeDetails = new HorizontalLayout();
        employeeDetails.setPadding(true);
        employeeDetails.addClassNames(LumoUtility.Padding.Bottom.SMALL);

        Avatar employeeAvatar = new Avatar();
        employeeAvatar.setWidth(60, Unit.PIXELS);
        employeeAvatar.setHeight(60, Unit.PIXELS);
        employeeAvatar.setImage("images/avatars/cody_fisher.jpg");
        H3 employeeTitle = new H3("Cody Fisher");
        Span employeeRole = new Span("Scrum Master");
        VerticalLayout employeeDetailsWrapper = new VerticalLayout(employeeTitle, employeeRole);
        employeeDetailsWrapper.setPadding(false);
        employeeDetailsWrapper.setSpacing(false);
        employeeDetailsWrapper.setJustifyContentMode(JustifyContentMode.CENTER);
        employeeDetails.add(employeeAvatar, employeeDetailsWrapper);

        Tabs tabs = new Tabs();
        tabs.setWidthFull();
        Tab personal = new Tab("Personal");
        Tab job = new Tab("Job");
        Tab emergency = new Tab("Emergency");
        Tab documents = new Tab("Documents");
        tabs.add(personal, job, emergency, documents);

        detailHeader.add(employeeDetails, tabs);

        /* Set the header for detail pane */
        detail.setHeader(detailHeader);


        /* Set the content for detail pane*/
        /* TODO Add other tabs and handling switching */
        detail.setContent(buildPersonalInfo());

        HorizontalLayout detailFooter = new HorizontalLayout();
        detailFooter.addClassName(LumoUtility.Gap.SMALL);
        Button edit = new Button("Edit");
        Button share = new Button("Share");
        Button delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        detailFooter.add(edit, share, delete);
        detail.setFooter(detailFooter);

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
        HorizontalLayout manager = createEmployeeItem("Esther Howard", "Software Development Manager", "images/avatars/esther_howard.jpg");
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

    private HorizontalLayout createEmployeeItem(String name, String role, String avatarImageUrl) {
        Avatar avatar = new Avatar(name);
        avatar.setImage(avatarImageUrl);
        HorizontalLayout employeeItem = new HorizontalLayout();
        employeeItem.setAlignItems(Alignment.CENTER);
        employeeItem.addClassNames(LumoUtility.Padding.Vertical.SMALL, LumoUtility.Gap.SMALL);

        VerticalLayout details = new VerticalLayout();
        details.addClassName(LumoUtility.Gap.XSMALL);
        details.setSpacing(false);
        details.setPadding(false);
        Span nameComponent = new Span(name);
        nameComponent.addClassNames(LumoUtility.LineHeight.NONE, LumoUtility.FontWeight.MEDIUM);
        Span roleComponent = new Span(role);
        roleComponent.addClassNames(LumoUtility.LineHeight.NONE, LumoUtility.FontSize.XSMALL, LumoUtility.TextColor.SECONDARY);

        details.add(nameComponent, roleComponent);

        employeeItem.add(avatar, details);

        return employeeItem;
    }
    private ComponentRenderer<Component, Employee> employeeCardRenderer = new ComponentRenderer<>(
            employee -> {
                HorizontalLayout employeeItem = createEmployeeItem(employee.getName(), employee.getRole(), "images/avatars/" + employee.getProfilePicUrl());
                employeeItem.addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.Padding.Horizontal.MEDIUM);
                return employeeItem;
            }
    );


}