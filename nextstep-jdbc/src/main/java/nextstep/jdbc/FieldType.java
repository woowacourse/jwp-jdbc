package nextstep.jdbc;

import java.util.Arrays;
import java.util.List;

public enum FieldType {
    BOOLEAN(Boolean.class),
    BYTE(Byte.class),
    CHARACTER(Character.class),
    SHORT(Short.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    DOUBLE(Double.class),
    FLOAT(Float.class),
    STRING(String.class);

    private Class<?> type;

    FieldType(Class<?> type) {
        this.type = type;
    }

    private static List<FieldType> fieldTypes = Arrays.asList(values());

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || isWrapper(type);
    }

    private static boolean isWrapper(Class<?> type) {
        for (FieldType fieldType : values()) {
            if (fieldType.type == type) {
                return true;
            }
        }

        return false;
    }
}
