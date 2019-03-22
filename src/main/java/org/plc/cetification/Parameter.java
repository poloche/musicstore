package org.plc.cetification;

public class Parameter {
    private final ParameterType type;
    private final Object value;
    private LikeType likeType;

    public Parameter(ParameterType like, String value, LikeType likeType) {
        this(like, value);
        this.likeType = likeType;
    }

    public ParameterType getType() {
        return type;
    }

    public LikeType getLikeType() {
        return likeType;
    }

    public Values getValues() {
        return (Values) value;
    }

    enum ParameterType {
        String,
        Like,
        IN, Int
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
