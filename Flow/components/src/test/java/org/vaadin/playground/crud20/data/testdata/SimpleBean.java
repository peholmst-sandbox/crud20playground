package org.vaadin.playground.crud20.data.testdata;

import org.vaadin.playground.crud20.data.property.source.annotation.GeneratePropertyMetadata;

import java.util.Objects;

@GeneratePropertyMetadata
public class SimpleBean {
    private String stringProperty;
    private int integerProperty;
    private boolean booleanProperty;

    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public int getIntegerProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(int integerProperty) {
        this.integerProperty = integerProperty;
    }

    public boolean isBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    @Override
    public String toString() {
        return "SimpleBean{" +
                "stringProperty='" + stringProperty + '\'' +
                ", integerProperty=" + integerProperty +
                ", booleanProperty=" + booleanProperty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleBean that = (SimpleBean) o;
        return integerProperty == that.integerProperty && booleanProperty == that.booleanProperty && Objects.equals(stringProperty, that.stringProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringProperty, integerProperty, booleanProperty);
    }
}
