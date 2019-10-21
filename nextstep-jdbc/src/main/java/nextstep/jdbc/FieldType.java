package nextstep.jdbc;

public enum FieldType {
    STRING(String.class),
    INTEGER(Integer.TYPE),
    BOOLEAN(Boolean.TYPE),
    LONG(Long.TYPE),
    DOUBLE(Double.TYPE),
    SHORT(Short.TYPE),
    CHARACTER(Character.TYPE),
    BYTE(Byte.TYPE),
    FLOAT(Float.TYPE);

    private Class<?> clazz;

    FieldType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static boolean isPrimitiveOrWrapped(Class<?> clazz) {
        return clazz.isPrimitive() || isWrapped(clazz);
    }

    private static boolean isWrapped(Class<?> clazz) {
        for (FieldType value : values()) {
            if (value.clazz == clazz) {
                return true;
            }
        }
        return false;
    }
}
