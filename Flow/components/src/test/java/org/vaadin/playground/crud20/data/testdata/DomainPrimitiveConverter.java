package org.vaadin.playground.crud20.data.testdata;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import jakarta.annotation.Nonnull;

public class DomainPrimitiveConverter implements Converter<String, DomainPrimitive> {

    private static final DomainPrimitiveConverter INSTANCE = new DomainPrimitiveConverter();

    public static @Nonnull DomainPrimitiveConverter instance() {
        return INSTANCE;
    }

    @Override
    public Result<DomainPrimitive> convertToModel(String s, ValueContext valueContext) {
        try {
            return (s == null || s.isEmpty()) ? Result.ok(null) : Result.ok(new DomainPrimitive(s));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    @Override
    public String convertToPresentation(DomainPrimitive testDomainPrimitive, ValueContext valueContext) {
        return testDomainPrimitive == null ? "" : testDomainPrimitive.value();
    }
}
