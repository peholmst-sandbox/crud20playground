package org.vaadin.playground.crud20.data.property.source;

import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.testdata.SimpleRecord;
import org.vaadin.playground.crud20.data.testdata.SimpleRecordMetadata;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleRecordPropertySourceTest {

    @Test
    void all_properties_are_empty_before_reading() {
        var source = PropertySource.forRecord(SimpleRecord.class);
        var stringProperty = source.forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forProperty(SimpleRecordMetadata.booleanProperty);
        {
            assertThat(stringProperty.isEmpty()).isTrue();
            assertThat(integerProperty.isEmpty()).isTrue();
            assertThat(booleanProperty.isEmpty()).isTrue();
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void reading_a_record_populates_the_properties() {
        var source = PropertySource.forRecord(SimpleRecord.class);
        var stringProperty = source.forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forProperty(SimpleRecordMetadata.booleanProperty);

        source.read(new SimpleRecord("string", 42, true));
        {
            assertThat(stringProperty.value()).isEqualTo("string");
            assertThat(integerProperty.value()).isEqualTo(42);
            assertThat(booleanProperty.value()).isTrue();
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void modifying_a_property_turns_the_source_dirty() {
        var source = PropertySource.forRecord(SimpleRecord.class);
        var stringProperty = source.forProperty(SimpleRecordMetadata.stringProperty);
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
        var source = PropertySource.forRecord(SimpleRecord.class);
        var stringProperty = source.forProperty(SimpleRecordMetadata.stringProperty);
        stringProperty.set("hello");
        {
            assertThat(source.dirty().value()).isTrue();
        }
        source.read(new SimpleRecord("string", 42, true));
        {
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void writing_without_reading_creates_an_empty_record() {
        var source = PropertySource.forRecord(SimpleRecord.class);
        var output = source.write();
        {
            assertThat(output.booleanProperty()).isFalse();
            assertThat(output.integerProperty()).isEqualTo(0);
            assertThat(output.stringProperty()).isNull();
        }
    }

    @Test
    void reading_and_writing_without_changes_produces_equal_records() {
        var source = PropertySource.forRecord(SimpleRecord.class);
        var original = new SimpleRecord("string", 42, true);
        source.read(original);
        var output = source.write();
        {
            assertThat(output).isEqualTo(original);
        }
    }

    @Test
    void changes_in_properties_are_reflected_in_written_record() {
        var source = PropertySource.forRecord(SimpleRecord.class);
        var stringProperty = source.forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forProperty(SimpleRecordMetadata.booleanProperty);
        stringProperty.set("string2");
        integerProperty.set(43);
        booleanProperty.set(false);

        var output = source.write();
        {
            assertThat(output.stringProperty()).isEqualTo("string2");
            assertThat(output.integerProperty()).isEqualTo(43);
            assertThat(output.booleanProperty()).isFalse();
        }
    }
}
