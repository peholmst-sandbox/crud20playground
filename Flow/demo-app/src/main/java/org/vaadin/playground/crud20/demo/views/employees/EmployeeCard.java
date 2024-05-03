package org.vaadin.playground.crud20.demo.views.employees;

import org.vaadin.playground.crud20.components.TwoLineCard;
import org.vaadin.playground.crud20.demo.sampledata.Employee;

class EmployeeCard extends TwoLineCard {

    public EmployeeCard(Employee employee) {
        setAvatar(employee.name(), "images/avatars/" + employee.profilePicUrl());
        setPrimaryLineText(employee.name());
        setSecondaryLineText(employee.role());
    }
}
