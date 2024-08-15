package org.vaadin.playground.crud20.demo.employee.domain.spi;

import net.pkhapps.commons.domain.primitives.NanoId;
import org.vaadin.playground.crud20.demo.common.domain.HasPublicIdRepository;
import org.vaadin.playground.crud20.demo.employee.domain.Employee;
import org.vaadin.playground.crud20.demo.employee.domain.EmployeeId;

public interface EmployeeRepository extends HasPublicIdRepository<EmployeeId, NanoId, Employee> {
}
