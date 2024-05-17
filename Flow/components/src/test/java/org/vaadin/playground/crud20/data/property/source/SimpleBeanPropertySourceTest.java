package org.vaadin.playground.crud20.data.property.source;

import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.testdata.SimpleBean;
import org.vaadin.playground.crud20.data.testdata.SimpleBeanMetadata;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleBeanPropertySourceTest {

    @Test
    void all_properties_are_empty_before_reading() {
        var source = PropertySource.forBean(SimpleBean.class);
        var stringProperty = source.forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty = source.forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty = source.forProperty(SimpleBeanMetadata.booleanProperty);
        {
            assertThat(stringProperty.isEmpty()).isTrue();
            assertThat(integerProperty.isEmpty()).isTrue();
            assertThat(booleanProperty.isEmpty()).isTrue();
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void reading_a_bean_populates_the_properties() {
        var source = PropertySource.forBean(SimpleBean.class);
        var stringProperty = source.forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty = source.forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty = source.forProperty(SimpleBeanMetadata.booleanProperty);

        var bean = new SimpleBean();
        bean.setStringProperty("string");
        bean.setIntegerProperty(42);
        bean.setBooleanProperty(true);

        source.read(bean);
        {
            assertThat(stringProperty.value()).isEqualTo("string");
            assertThat(integerProperty.value()).isEqualTo(42);
            assertThat(booleanProperty.value()).isTrue();
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void only_properties_added_before_reading_are_populated() {
        var source = PropertySource.forBean(SimpleBean.class);
        var stringProperty = source.forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty = source.forProperty(SimpleBeanMetadata.integerProperty);

        var bean = new SimpleBean();
        bean.setStringProperty("string");
        bean.setIntegerProperty(42);
        bean.setBooleanProperty(true);

        source.read(bean);
        var booleanProperty = source.forProperty(SimpleBeanMetadata.booleanProperty);
        {
            assertThat(stringProperty.value()).isEqualTo("string");
            assertThat(integerProperty.value()).isEqualTo(42);
            assertThat(booleanProperty.isEmpty()).isTrue();
        }
    }

    @Test
    void modifying_a_property_turns_the_source_dirty() {
        var source = PropertySource.forBean(SimpleBean.class);
        var stringProperty = source.forProperty(SimpleBeanMetadata.stringProperty);
        {
            assertThat(source.dirty().value()).isFalse();
        }
        stringProperty.set("hello");
        {
            assertThat(source.dirty().value()).isTrue();
        }
    }

    @Test
    void reading_a_bean_turns_the_source_clean() {
        var source = PropertySource.forBean(SimpleBean.class);
        var stringProperty = source.forProperty(SimpleBeanMetadata.stringProperty);
        stringProperty.set("hello");
        {
            assertThat(source.dirty().value()).isTrue();
        }
        source.read(new SimpleBean());
        {
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void writing_without_registering_properties_creates_an_empty_bean() {
        var source = PropertySource.forBean(SimpleBean.class);
        var output = source.write();
        {
            assertThat(output.getStringProperty()).isNull();
            assertThat(output.getIntegerProperty()).isEqualTo(0);
            assertThat(output.isBooleanProperty()).isFalse();
        }
    }

    @Test
    void writing_without_reading_creates_an_empty_bean() {
        var source = PropertySource.forBean(SimpleBean.class);
        source.forProperty(SimpleBeanMetadata.stringProperty);
        source.forProperty(SimpleBeanMetadata.integerProperty);
        source.forProperty(SimpleBeanMetadata.booleanProperty);
        var output = source.write();
        {
            assertThat(output.getStringProperty()).isNull();
            assertThat(output.getIntegerProperty()).isEqualTo(0);
            assertThat(output.isBooleanProperty()).isFalse();
        }
    }

    @Test
    void reading_and_writing_without_changes_produces_equal_beans() {
        var source = PropertySource.forBean(SimpleBean.class);
        source.forProperty(SimpleBeanMetadata.stringProperty);
        source.forProperty(SimpleBeanMetadata.integerProperty);
        source.forProperty(SimpleBeanMetadata.booleanProperty);

        var bean = new SimpleBean();
        bean.setStringProperty("string");
        bean.setIntegerProperty(42);
        bean.setBooleanProperty(true);

        source.read(bean);
        var output = source.write();
        {
            assertThat(output).isNotSameAs(bean);
            assertThat(output.isBooleanProperty()).isEqualTo(bean.isBooleanProperty());
            assertThat(output.getStringProperty()).isEqualTo(bean.getStringProperty());
            assertThat(output.getIntegerProperty()).isEqualTo(bean.getIntegerProperty());
        }
    }

    @Test
    void changes_in_properties_are_reflected_in_written_bean() {
        var source = PropertySource.forBean(SimpleBean.class);
        var stringProperty = source.forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty = source.forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty = source.forProperty(SimpleBeanMetadata.booleanProperty);

        stringProperty.set("string2");
        integerProperty.set(43);
        booleanProperty.set(false);

        var output = source.write();
        {
            assertThat(output.getStringProperty()).isEqualTo("string2");
            assertThat(output.getIntegerProperty()).isEqualTo(43);
            assertThat(output.isBooleanProperty()).isFalse();
        }
    }

    @Test
    void changes_can_also_be_written_to_an_existing_bean_instance() {
        var source = PropertySource.forBean(SimpleBean.class);
        var stringProperty = source.forProperty(SimpleBeanMetadata.stringProperty);

        var bean = new SimpleBean();
        bean.setStringProperty("string");
        bean.setIntegerProperty(42);
        bean.setBooleanProperty(true);

        source.read(bean);
        stringProperty.set("string2");
        source.write(bean);
        {
            assertThat(bean.getStringProperty()).isEqualTo("string2");
            assertThat(bean.getIntegerProperty()).isEqualTo(42);
            assertThat(bean.isBooleanProperty()).isTrue();
        }
    }
}
