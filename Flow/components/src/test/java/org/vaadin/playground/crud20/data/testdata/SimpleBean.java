package org.vaadin.playground.crud20.data.testdata;

import org.vaadin.playground.crud20.data.property.source.annotation.GeneratePropertyMetadata;

@GeneratePropertyMetadata
public class SimpleBean {
    private String stringProperty;
    private Integer integerProperty;
    private Boolean booleanProperty;
    private long longProperty;

    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public Integer getIntegerProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(Integer integerProperty) {
        this.integerProperty = integerProperty;
    }

    public Boolean getBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(Boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public long getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(long longProperty) {
        this.longProperty = longProperty;
    }
}
