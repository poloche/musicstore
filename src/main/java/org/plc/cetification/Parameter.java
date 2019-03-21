package org.plc.cetification;

public class Parameter {
    private final ParameterType type;
    private final Object value;

    public ParameterType getType() {
        return type;
    }

    enum ParameterType {
        String,
        Int
    }

    public Parameter(ParameterType type, Object value) {
        this.type = type;
        this.value = value;
    }

    int getIntValue() {
        return (Integer) value;
    }

    String getStringValue() {
        return (String) value;
    }
}
