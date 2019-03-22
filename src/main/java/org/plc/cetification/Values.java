package org.plc.cetification;

import java.util.List;

public class Values<T> {
    private final Type type;
    private final List<T> values;

    enum Type {
        INT,
        STRING
    }

    public Values(Values.Type type, List<T> values) {
        this.type = type;
        this.values = values;
    }

    public List<T> getValues() {
        return values;
    }

    public Type getType() {
        return type;
    }
}
