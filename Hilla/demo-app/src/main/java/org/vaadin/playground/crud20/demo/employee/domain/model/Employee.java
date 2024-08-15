package org.vaadin.playground.crud20.demo.employee.domain.model;

import io.soabase.recordbuilder.core.RecordBuilder;
import net.pkhapps.commons.domain.primitives.EmailAddress;
import net.pkhapps.commons.domain.primitives.NanoId;
import net.pkhapps.commons.domain.primitives.PhoneNumber;
import net.pkhapps.commons.domain.primitives.geo.Address;
import org.jetbrains.annotations.NotNull;
import org.vaadin.playground.crud20.demo.common.domain.model.AggregateRoot;
import org.vaadin.playground.crud20.demo.common.domain.model.HasPublicId;
import org.vaadin.playground.crud20.demo.employee.domain.primitives.EmployeeId;
import org.vaadin.playground.crud20.demo.jooq.enums.Gender;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.truncate;
import static org.apache.commons.lang3.Validate.notBlank;

@RecordBuilder
public record Employee(
        EmployeeId id,
        NanoId publicId,
        long optimisticLockingVersion,
        @NotNull String firstName,
        String middleName,
        @NotNull String lastName,
        String preferredName,
        LocalDate birthDate,
        Gender gender,
        String dietaryNotes,
        Address homeAddress,
        PhoneNumber workPhone,
        PhoneNumber mobilePhone,
        PhoneNumber homePhone,
        EmailAddress workEmail
) implements AggregateRoot<EmployeeId>, HasPublicId<NanoId> {

    // Records like this one are likely going to benefit significantly from https://bugs.openjdk.org/browse/JDK-8321133
    // In the meantime, we'll use RecordBuilder (https://github.com/randgalt/record-builder)

    // TODO Optimistic locking?

    public static final int FIRST_NAME_MAX_WIDTH = 100;
    public static final int MIDDLE_NAME_MAX_WIDTH = 100;
    public static final int LAST_NAME_MAX_WIDTH = 100;
    public static final int PREFERRED_NAME_MAX_WIDTH = 100;

    public Employee(EmployeeId id,
                    NanoId publicId,
                    long optimisticLockingVersion,
                    @NotNull String firstName,
                    String middleName,
                    @NotNull String lastName,
                    String preferredName,
                    LocalDate birthDate,
                    Gender gender,
                    String dietaryNotes,
                    Address homeAddress,
                    PhoneNumber workPhone,
                    PhoneNumber mobilePhone,
                    PhoneNumber homePhone,
                    EmailAddress workEmail) {
        this.id = id;
        this.publicId = publicId;
        this.optimisticLockingVersion = optimisticLockingVersion;
        this.firstName = truncate(notBlank(firstName), FIRST_NAME_MAX_WIDTH);
        this.middleName = truncate(middleName, MIDDLE_NAME_MAX_WIDTH);
        this.lastName = truncate(notBlank(lastName), LAST_NAME_MAX_WIDTH);
        this.preferredName = truncate(preferredName, PREFERRED_NAME_MAX_WIDTH);
        this.birthDate = birthDate; // TODO How and when to verify that this date is not in the future?
        this.gender = gender;
        this.dietaryNotes = dietaryNotes;
        this.homeAddress = homeAddress;
        this.workPhone = workPhone;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.workEmail = workEmail;
    }

    // TODO What about equals and hashCode? Is there a need to override them to check the ID only?
}
