package org.vaadin.playground.crud20.data.property.source;

import org.junit.jupiter.api.Test;
import org.vaadin.playground.crud20.data.testdata.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplexBeanPropertySourceTest {

    private static ComplexBean createExampleBean() {
        var bean = new ComplexBean();
        bean.setLocalDateTimeProperty(LocalDateTime.of(2024, 5, 17, 14, 19));
        bean.setBeanProperty(new SimpleBean());
        bean.getBeanProperty().setStringProperty("string");
        bean.getBeanProperty().setIntegerProperty(42);
        bean.getBeanProperty().setBooleanProperty(true);
        bean.getReadonlyBeanProperty().setStringProperty("string2");
        bean.getReadonlyBeanProperty().setIntegerProperty(43);
        bean.getReadonlyBeanProperty().setBooleanProperty(false);
        bean.setDomainPrimitiveProperty(new DomainPrimitive("hello"));
        bean.setRecordProperty(new SimpleRecord("string3", 44, true));
        return bean;
    }

    @Test
    void all_properties_are_empty_before_reading() {
        var source = PropertySource.forBean(ComplexBean.class);
        var localDateTimeProperty = source.forProperty(ComplexBeanMetadata.localDateTimeProperty);
        var beanProperty = source.forProperty(ComplexBeanMetadata.beanProperty);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.booleanProperty);
        var stringProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.booleanProperty);
        var domainPrimitiveProperty = source.forProperty(ComplexBeanMetadata.domainPrimitiveProperty);
        var recordProperty = source.forProperty(ComplexBeanMetadata.recordProperty);
        var recordStringProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var recordIntegerProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var recordBooleanProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        {
            assertThat(localDateTimeProperty.isEmpty()).isTrue();
            assertThat(beanProperty.isEmpty()).isTrue();
            assertThat(stringProperty.isEmpty()).isTrue();
            assertThat(integerProperty.isEmpty()).isTrue();
            assertThat(booleanProperty.isEmpty()).isTrue();
            assertThat(stringProperty2.isEmpty()).isTrue();
            assertThat(integerProperty2.isEmpty()).isTrue();
            assertThat(booleanProperty2.isEmpty()).isTrue();
            assertThat(domainPrimitiveProperty.isEmpty()).isTrue();
            assertThat(recordProperty.isEmpty()).isTrue();
            assertThat(recordStringProperty.isEmpty()).isTrue();
            assertThat(recordIntegerProperty.isEmpty()).isTrue();
            assertThat(recordBooleanProperty.isEmpty()).isTrue();
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void reading_a_bean_populates_the_properties() {
        var source = PropertySource.forBean(ComplexBean.class);
        var localDateTimeProperty = source.forProperty(ComplexBeanMetadata.localDateTimeProperty);
        var beanProperty = source.forProperty(ComplexBeanMetadata.beanProperty);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.booleanProperty);
        var stringProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.booleanProperty);
        var domainPrimitiveProperty = source.forProperty(ComplexBeanMetadata.domainPrimitiveProperty);
        var recordProperty = source.forProperty(ComplexBeanMetadata.recordProperty);
        var recordStringProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var recordIntegerProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var recordBooleanProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);

        var bean = createExampleBean();
        source.read(bean);
        {
            assertThat(localDateTimeProperty.value()).isEqualTo(bean.getLocalDateTimeProperty());
            assertThat(beanProperty.value()).isEqualTo(bean.getBeanProperty());
            assertThat(stringProperty.value()).isEqualTo(bean.getBeanProperty().getStringProperty());
            assertThat(integerProperty.value()).isEqualTo(bean.getBeanProperty().getIntegerProperty());
            assertThat(booleanProperty.value()).isEqualTo(bean.getBeanProperty().isBooleanProperty());
            assertThat(stringProperty2.value()).isEqualTo(bean.getReadonlyBeanProperty().getStringProperty());
            assertThat(integerProperty2.value()).isEqualTo(bean.getReadonlyBeanProperty().getIntegerProperty());
            assertThat(booleanProperty2.value()).isEqualTo(bean.getReadonlyBeanProperty().isBooleanProperty());
            assertThat(domainPrimitiveProperty.value()).isEqualTo(bean.getDomainPrimitiveProperty());
            assertThat(recordProperty.value()).isEqualTo(bean.getRecordProperty());
            assertThat(recordStringProperty.value()).isEqualTo(bean.getRecordProperty().stringProperty());
            assertThat(recordIntegerProperty.value()).isEqualTo(bean.getRecordProperty().integerProperty());
            assertThat(recordBooleanProperty.value()).isEqualTo(bean.getRecordProperty().booleanProperty());
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void modifying_a_property_turns_the_source_dirty() {
        var source = PropertySource.forBean(ComplexBean.class);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        {
            assertThat(source.dirty().value()).isFalse();
        }
        stringProperty.set("hello");
        {
            assertThat(source.dirty().value()).isTrue();
        }
    }

    @Test
    void modifying_a_property_of_a_readonly_bean_also_turns_the_source_dirty() {
        var source = PropertySource.forBean(ComplexBean.class);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.stringProperty);
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
        var source = PropertySource.forBean(ComplexBean.class);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        stringProperty.set("hello");
        {
            assertThat(source.dirty().value()).isTrue();
        }
        source.read(createExampleBean());
        {
            assertThat(source.dirty().value()).isFalse();
        }
    }

    @Test
    void writing_without_reading_creates_an_empty_bean() {
        var source = PropertySource.forBean(ComplexBean.class);
        source.forProperty(ComplexBeanMetadata.localDateTimeProperty);
        source.forProperty(ComplexBeanMetadata.beanProperty);
        source.forProperty(ComplexBeanMetadata.readonlyBeanProperty);
        source.forProperty(ComplexBeanMetadata.domainPrimitiveProperty);
        source.forProperty(ComplexBeanMetadata.recordProperty);

        var output = source.write();
        {
            assertThat(output.getLocalDateTimeProperty()).isNull();
            assertThat(output.getBeanProperty()).isNull();
            assertThat(output.getReadonlyBeanProperty().getStringProperty()).isNull();
            assertThat(output.getReadonlyBeanProperty().getIntegerProperty()).isEqualTo(0);
            assertThat(output.getReadonlyBeanProperty().isBooleanProperty()).isFalse();
            assertThat(output.getDomainPrimitiveProperty()).isNull();
            assertThat(output.getRecordProperty()).isNull();
        }
    }

    @Test
    void reading_and_writing_without_changes_produces_equal_beans() {
        var source = PropertySource.forBean(ComplexBean.class);
        source.forProperty(ComplexBeanMetadata.localDateTimeProperty);
        source.forProperty(ComplexBeanMetadata.beanProperty);
        {
            // A read-only property cannot be written back. The only way to modify is it to modify its properties.
            // In order to do that, we need to register every property of the read-only bean.
            source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.stringProperty);
            source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.integerProperty);
            source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.booleanProperty);
        }
        source.forProperty(ComplexBeanMetadata.domainPrimitiveProperty);
        source.forProperty(ComplexBeanMetadata.recordProperty);
        var bean = createExampleBean();
        source.read(bean);
        var output = source.write();
        {
            assertThat(output).isEqualTo(bean);
            assertThat(output).isNotSameAs(bean);
        }

    }

    @Test
    void changes_in_properties_are_reflected_in_written_bean() {
        var source = PropertySource.forBean(ComplexBean.class);
        var localDateTimeProperty = source.forProperty(ComplexBeanMetadata.localDateTimeProperty);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.booleanProperty);
        var stringProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        var integerProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.integerProperty);
        var booleanProperty2 = source.forBeanProperty(ComplexBeanMetadata.readonlyBeanProperty).forProperty(SimpleBeanMetadata.booleanProperty);
        var domainPrimitiveProperty = source.forProperty(ComplexBeanMetadata.domainPrimitiveProperty);
        var recordStringProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var recordIntegerProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var recordBooleanProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        localDateTimeProperty.set(LocalDateTime.of(2024, 5, 17, 11, 7));
        stringProperty.set("hello");
        integerProperty.set(43);
        booleanProperty.set(true);
        stringProperty2.set("hello2");
        integerProperty2.set(44);
        booleanProperty2.set(true);
        domainPrimitiveProperty.set(new DomainPrimitive("world"));
        recordStringProperty.set("hello3");
        recordIntegerProperty.set(45);
        recordBooleanProperty.set(true);

        var output = source.write();
        {
            assertThat(output.getLocalDateTimeProperty()).isEqualTo(LocalDateTime.of(2024, 5, 17, 11, 7));
            assertThat(output.getBeanProperty().getStringProperty()).isEqualTo("hello");
            assertThat(output.getBeanProperty().getIntegerProperty()).isEqualTo(43);
            assertThat(output.getBeanProperty().isBooleanProperty()).isTrue();
            assertThat(output.getReadonlyBeanProperty().getStringProperty()).isEqualTo("hello2");
            assertThat(output.getReadonlyBeanProperty().getIntegerProperty()).isEqualTo(44);
            assertThat(output.getReadonlyBeanProperty().isBooleanProperty()).isTrue();
            assertThat(output.getDomainPrimitiveProperty()).isEqualTo(new DomainPrimitive("world"));
            assertThat(output.getRecordProperty().stringProperty()).isEqualTo("hello3");
            assertThat(output.getRecordProperty().integerProperty()).isEqualTo(45);
            assertThat(output.getRecordProperty().booleanProperty()).isTrue();
        }
    }

    @Test
    void bean_properties_can_be_modified_as_both_beans_and_as_individual_properties() {
        var source = PropertySource.forBean(ComplexBean.class);
        var beanProperty = source.forProperty(ComplexBeanMetadata.beanProperty);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);

        var simpleBean = new SimpleBean();
        simpleBean.setStringProperty("hello");

        beanProperty.set(simpleBean);
        {
            assertThat(stringProperty.value()).isEqualTo("hello");
        }

        stringProperty.set("world");
        {
            assertThat(beanProperty.value().getStringProperty()).isEqualTo("world");
        }
    }

    @Test
    void record_properties_can_be_modified_as_both_records_and_as_individual_properties() {
        var source = PropertySource.forBean(ComplexBean.class);
        var recordProperty = source.forProperty(ComplexBeanMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);

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
    void clearing_a_bean_property_clears_its_individual_properties() {
        var source = PropertySource.forBean(ComplexBean.class);
        var beanProperty = source.forProperty(ComplexBeanMetadata.beanProperty);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        source.read(createExampleBean());
        beanProperty.clear();
        {
            assertThat(stringProperty.isEmpty()).isTrue();
        }
    }

    @Test
    void clearing_a_record_property_clears_its_individual_properties() {
        var source = PropertySource.forBean(ComplexBean.class);
        var recordProperty = source.forProperty(ComplexBeanMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        source.read(createExampleBean());
        recordProperty.clear();
        {
            assertThat(stringProperty.isEmpty()).isTrue();
            assertThat(integerProperty.isEmpty()).isTrue();
            assertThat(booleanProperty.isEmpty()).isTrue();
        }
    }

    @Test
    void clearing_individual_properties_also_clears_the_bean_property() {
        var source = PropertySource.forBean(ComplexBean.class);
        var beanProperty = source.forProperty(ComplexBeanMetadata.beanProperty);
        var stringProperty = source.forBeanProperty(ComplexBeanMetadata.beanProperty).forProperty(SimpleBeanMetadata.stringProperty);
        source.read(createExampleBean());
        stringProperty.clear();
        {
            assertThat(beanProperty.isEmpty()).isTrue();
        }
    }

    @Test
    void clearing_individual_properties_also_clears_the_record_property() {
        var source = PropertySource.forBean(ComplexBean.class);
        var recordProperty = source.forProperty(ComplexBeanMetadata.recordProperty);
        var stringProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.stringProperty);
        var integerProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.integerProperty);
        var booleanProperty = source.forRecordProperty(ComplexBeanMetadata.recordProperty).forProperty(SimpleRecordMetadata.booleanProperty);
        source.read(createExampleBean());
        stringProperty.clear();
        integerProperty.clear();
        booleanProperty.clear();
        {
            assertThat(recordProperty.isEmpty()).isTrue();
        }
    }
}
