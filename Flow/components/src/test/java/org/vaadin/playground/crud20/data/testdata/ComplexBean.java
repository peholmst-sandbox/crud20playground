package org.vaadin.playground.crud20.data.testdata;

import org.vaadin.playground.crud20.data.property.source.annotation.GeneratePropertyMetadata;

import java.time.LocalDateTime;
import java.util.Objects;

@GeneratePropertyMetadata
public class ComplexBean {
    private LocalDateTime localDateTimeProperty;
    private SimpleBean beanProperty;
    private final SimpleBean readonlyBeanProperty = new SimpleBean();
    private DomainPrimitive domainPrimitiveProperty;
    private SimpleRecord recordProperty;

    public LocalDateTime getLocalDateTimeProperty() {
        return localDateTimeProperty;
    }

    public void setLocalDateTimeProperty(LocalDateTime localDateTimeProperty) {
        this.localDateTimeProperty = localDateTimeProperty;
    }

    public SimpleBean getBeanProperty() {
        return beanProperty;
    }

    public void setBeanProperty(SimpleBean beanProperty) {
        this.beanProperty = beanProperty;
    }

    public SimpleBean getReadonlyBeanProperty() {
        return readonlyBeanProperty;
    }

    public DomainPrimitive getDomainPrimitiveProperty() {
        return domainPrimitiveProperty;
    }

    public void setDomainPrimitiveProperty(DomainPrimitive domainPrimitiveProperty) {
        this.domainPrimitiveProperty = domainPrimitiveProperty;
    }

    public SimpleRecord getRecordProperty() {
        return recordProperty;
    }

    public void setRecordProperty(SimpleRecord recordProperty) {
        this.recordProperty = recordProperty;
    }

    @Override
    public String toString() {
        return "ComplexBean{" +
                "localDateTimeProperty=" + localDateTimeProperty +
                ", beanProperty=" + beanProperty +
                ", readonlyBeanProperty=" + readonlyBeanProperty +
                ", domainPrimitiveProperty=" + domainPrimitiveProperty +
                ", recordProperty=" + recordProperty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexBean that = (ComplexBean) o;
        return Objects.equals(localDateTimeProperty, that.localDateTimeProperty) && Objects.equals(beanProperty, that.beanProperty) && Objects.equals(readonlyBeanProperty, that.readonlyBeanProperty) && Objects.equals(domainPrimitiveProperty, that.domainPrimitiveProperty) && Objects.equals(recordProperty, that.recordProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDateTimeProperty, beanProperty, readonlyBeanProperty, domainPrimitiveProperty, recordProperty);
    }
}
