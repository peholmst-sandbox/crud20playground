package org.vaadin.playground.crud20.data.testdata;

import org.vaadin.playground.crud20.data.property.source.annotation.GeneratePropertyMetadata;

import java.time.LocalDateTime;

@GeneratePropertyMetadata
public record ComplexRecord(LocalDateTime localDateTimeProperty,
                            SimpleRecord recordProperty,
                            DomainPrimitive domainPrimitiveProperty) {
}
