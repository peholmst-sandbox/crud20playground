package org.vaadin.playground.crud20.demo.employee.domain.model;

import net.pkhapps.commons.domain.primitives.NanoId;
import org.vaadin.playground.crud20.demo.common.domain.model.HasPublicIdRepository;
import org.vaadin.playground.crud20.demo.employee.domain.primitives.EmployeeId;

public interface EmployeeRepository extends HasPublicIdRepository<EmployeeId, NanoId, Employee> {
}
