package org.vaadin.playground.crud20.data.testdata;

import org.vaadin.playground.crud20.data.property.source.annotation.GeneratePropertyMetadata;

@GeneratePropertyMetadata
public record SimpleRecord(String stringProperty,
                           int integerProperty,
                           boolean booleanProperty) {
}
