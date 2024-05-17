package org.vaadin.playground.crud20.data.property.source;

import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.testdata.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplexRecordPropertySourceTest {

    public static final ComplexRecord EXAMPLE_DATA = new ComplexRecord(
            LocalDateTime.now(),
            new SimpleRecord(
                    "string",
                    42,
                    true),
            new DomainPrimitive("hello")
    );

    @Test
    void all_properties_are_empty_before_reading() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var localDateTimeProperty = source.forProperty(ComplexRecordMetadata.localDateTimeProperty);
        var recordProperty = source.forProperty(ComplexRecordMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        var domainPrimitiveProperty = source.forProperty(ComplexRecordMetadata.domainPrimitiveProperty);
        {
            assertThat(localDateTimeProperty.isEmpty()).isTrue();
            assertThat(recordProperty.isEmpty()).isTrue();
            assertThat(stringProperty.isEmpty()).isTrue();
            assertThat(integerProperty.isEmpty()).isTrue();
            assertThat(booleanProperty.isEmpty()).isTrue();
            assertThat(domainPrimitiveProperty.isEmpty()).isTrue();
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void reading_a_record_populates_the_properties() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var localDateTimeProperty = source.forProperty(ComplexRecordMetadata.localDateTimeProperty);
        var recordProperty = source.forProperty(ComplexRecordMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        var domainPrimitiveProperty = source.forProperty(ComplexRecordMetadata.domainPrimitiveProperty);

        source.read(EXAMPLE_DATA);
        {
            assertThat(localDateTimeProperty.value()).isEqualTo(EXAMPLE_DATA.localDateTimeProperty());
            assertThat(recordProperty.value()).isEqualTo(EXAMPLE_DATA.recordProperty());
            assertThat(stringProperty.value()).isEqualTo(EXAMPLE_DATA.recordProperty().stringProperty());
            assertThat(integerProperty.value()).isEqualTo(EXAMPLE_DATA.recordProperty().integerProperty());
            assertThat(booleanProperty.value()).isEqualTo(EXAMPLE_DATA.recordProperty().booleanProperty());
            assertThat(domainPrimitiveProperty.value()).isEqualTo(EXAMPLE_DATA.domainPrimitiveProperty());
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void modifying_a_property_turns_the_source_dirty() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        {
            assertThat(source.dirty().value()).isFalse();
        }
        stringProperty.set("hello");
        {
            assertThat(source.dirty().value()).isTrue();
        }
    }

    @Test
    void reading_a_record_turns_the_source_clean() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        stringProperty.set("hello");
        {
            assertThat(source.dirty().value()).isTrue();
        }
        source.read(EXAMPLE_DATA);
        {
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void writing_without_reading_creates_an_empty_record() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var output = source.write();
        {
            assertThat(output.localDateTimeProperty()).isNull();
            assertThat(output.domainPrimitiveProperty()).isNull();
            assertThat(output.recordProperty()).isNull();
        }
    }

    @Test
    void reading_and_writing_without_changes_produces_equal_records() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        source.read(EXAMPLE_DATA);
        var output = source.write();
        {
            assertThat(output).isEqualTo(EXAMPLE_DATA);
        }
    }

    @Test
    void changes_in_properties_are_reflected_in_written_record() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var localDateTimeProperty = source.forProperty(ComplexRecordMetadata.localDateTimeProperty);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        var domainPrimitiveProperty = source.forProperty(ComplexRecordMetadata.domainPrimitiveProperty);
        localDateTimeProperty.set(LocalDateTime.of(2024, 5, 17, 11, 7));
        stringProperty.set("hello");
        integerProperty.set(43);
        booleanProperty.set(false);
        domainPrimitiveProperty.set(new DomainPrimitive("world"));

        var output = source.write();
        {
            assertThat(output.localDateTimeProperty()).isEqualTo(LocalDateTime.of(2024, 5, 17, 11, 7));
            assertThat(output.recordProperty().stringProperty()).isEqualTo("hello");
            assertThat(output.recordProperty().integerProperty()).isEqualTo(43);
            assertThat(output.recordProperty().booleanProperty()).isFalse();
            assertThat(output.domainPrimitiveProperty()).isEqualTo(new DomainPrimitive("world"));
        }
    }

    @Test
    void record_properties_can_be_modified_as_both_records_and_as_individual_properties() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var recordProperty = source.forProperty(ComplexRecordMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);

        recordProperty.set(new SimpleRecord("hello", 42, true));
        {
            assertThat(stringProperty.value()).isEqualTo("hello");
            assertThat(integerProperty.value()).isEqualTo(42);
            assertThat(booleanProperty.value()).isTrue();
        }

        stringProperty.set("world");
        integerProperty.set(43);
        booleanProperty.set(false);
        {
            assertThat(recordProperty.value().stringProperty()).isEqualTo("world");
            assertThat(recordProperty.value().integerProperty()).isEqualTo(43);
            assertThat(recordProperty.value().booleanProperty()).isFalse();
        }
    }

    @Test
    void clearing_a_record_property_clears_its_individual_properties() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var recordProperty = source.forProperty(ComplexRecordMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        source.read(EXAMPLE_DATA);
        recordProperty.clear();
        {
            assertThat(stringProperty.isEmpty()).isTrue();
            assertThat(integerProperty.isEmpty()).isTrue();
            assertThat(booleanProperty.isEmpty()).isTrue();
        }
    }

    @Test
    void clearing_individual_properties_also_clears_the_record_property() {
        var source = PropertySource.forRecord(ComplexRecord.class);
        var recordProperty = source.forProperty(ComplexRecordMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexRecordMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        source.read(EXAMPLE_DATA);
        stringProperty.clear();
        integerProperty.clear();
        booleanProperty.clear();
        {
            assertThat(recordProperty.isEmpty()).isTrue();
        }
    }
}
