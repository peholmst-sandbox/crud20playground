package org.vaadin.playground.crud20.data.testdata;

import org.vaadin.playground.crud20.data.property.source.annotation.GeneratePropertyMetadata;

import java.time.LocalDateTime;

@GeneratePropertyMetadata
public class ComplexBean {
    private LocalDateTime localDateTimeProperty;
    private SimpleBean beanProperty;
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
}
