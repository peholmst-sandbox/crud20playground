package org.vaadin.playground.crud20.demo.jooq;

import net.pkhapps.commons.domain.primitives.EmailAddress;
import net.pkhapps.commons.domain.primitives.PhoneNumber;
import net.pkhapps.commons.domain.primitives.geo.usa.USPostalAddress;
import net.pkhapps.commons.domain.primitives.geo.usa.USStateAndTerritory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.vaadin.playground.crud20.demo.employee.domain.EmployeeBuilder;
import org.vaadin.playground.crud20.demo.jooq.enums.Gender;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JooqEmployeeRepositoryIT {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    @Autowired
    JooqEmployeeRepository repository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void can_create_retrieve_update_and_delete_employee() {
        // CREATE
        var sizeBeforeInsert = repository.size();
        var inserted = repository.save(EmployeeBuilder.builder()
                .firstName("Joe")
                .middleName("Edgar")
                .lastName("Cool")
                .preferredName("Joe")
                .birthDate(LocalDate.of(1983, 9, 4))
                .gender(Gender.MALE)
                .dietaryNotes("Lactose intolerant")
                .homeAddress(USPostalAddress.builder()
                        .streetNumber("742")
                        .streetName("Evergreen Terrace")
                        .city("Springfield")
                        .state(USStateAndTerritory.IL)
                        .zipCode("62704")
                        .build())
                .workPhone(PhoneNumber.valueOf("+1 (312) 555-0198"))
                .mobilePhone(PhoneNumber.valueOf("+1 (618) 555-0145"))
                .homePhone(PhoneNumber.valueOf("+1 (217) 555-0137"))
                .workEmail(EmailAddress.valueOf("joe.cool@initech.foo"))
                .build());
        var sizeAfterInsert = repository.size();

        assertThat(sizeAfterInsert).isEqualTo(sizeBeforeInsert + 1);
        assertThat(inserted.isNew()).isFalse();
        assertThat(inserted.publicId()).isNotNull();
        assertThat(inserted.optimisticLockingVersion()).isEqualTo(1);
        assertThat(inserted.id()).isNotNull();
        assertThat(repository.containsById(inserted.requireId())).isTrue();
        assertThat(repository.findAuditLogById(inserted.requireId())).hasSize(1);

        // RETRIEVE
        assertThat(inserted).isEqualTo(repository.findById(inserted.requireId()).orElseThrow());

        // UPDATE
        var updated = repository.save(EmployeeBuilder.builder(inserted).preferredName("Mr. Cool").build());
        assertThat(updated.optimisticLockingVersion()).isEqualTo(2);
        assertThat(updated).isEqualTo(repository.findById(updated.requireId()).orElseThrow());
        assertThat(repository.findAuditLogById(updated.requireId())).hasSize(2);

        // DELETE
        repository.deleteById(updated.requireId());
        var sizeAfterDelete = repository.size();

        assertThat(sizeAfterDelete).isEqualTo(sizeAfterInsert - 1);
        assertThat(repository.containsById(updated.requireId())).isFalse();
        assertThat(repository.findAuditLogById(updated.requireId())).hasSize(3);
    }

    @Test
    void can_use_public_ids_to_identify_employees() {
        var inserted = repository.save(EmployeeBuilder.builder()
                .firstName("Joe")
                .middleName("Edgar")
                .lastName("Cool")
                .preferredName("Joe")
                .birthDate(LocalDate.of(1983, 9, 4))
                .gender(Gender.MALE)
                .dietaryNotes("Lactose intolerant")
                .homeAddress(USPostalAddress.builder()
                        .streetNumber("742")
                        .streetName("Evergreen Terrace")
                        .city("Springfield")
                        .state(USStateAndTerritory.IL)
                        .zipCode("62704")
                        .build())
                .workPhone(PhoneNumber.valueOf("+1 (312) 555-0198"))
                .mobilePhone(PhoneNumber.valueOf("+1 (618) 555-0145"))
                .homePhone(PhoneNumber.valueOf("+1 (217) 555-0137"))
                .workEmail(EmailAddress.valueOf("joe.cool@initech.foo"))
                .build());

        assertThat(repository.findByPublicId(inserted.requirePublicId())).contains(inserted);
        assertThat(repository.containsByPublicId(inserted.requirePublicId())).isTrue();
        assertThat(repository.resolveId(inserted.requirePublicId())).contains(inserted.requireId());

        repository.deleteByPublicId(inserted.requirePublicId());
        assertThat(repository.containsByPublicId(inserted.requirePublicId())).isFalse();
    }
}
