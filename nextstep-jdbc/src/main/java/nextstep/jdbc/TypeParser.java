package nextstep.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.BiFunction;

public enum TypeParser {
    INT(int.class, TypeParser::getInt, 0),
    LONG(long.class, TypeParser::getLong, 0L),
    BOOLEAN(boolean.class, TypeParser::getBoolean, false),
    BYTE(byte.class, TypeParser::getByte, 0),
    SHORT(short.class, TypeParser::getShort, 0),
    DOUBLE(double.class, TypeParser::getDouble, 0.0d),
    FLOAT(float.class, TypeParser::getFloat, 0.0f),
    STRING(String.class, TypeParser::getString, null);

    private Class<?> valueType;
    private BiFunction<ResultSet, String, Object> biFunction;
    private Object defaultValue;

    TypeParser(Class<?> valueType, BiFunction<ResultSet, String, Object> biFunction, Object defaultValue) {
        this.valueType = valueType;
        this.biFunction = biFunction;
        this.defaultValue = defaultValue;
    }

    public static Object parse(ResultSet resultSet, String name, Class<?> fieldType) {
        TypeParser parser = Arrays.stream(values())
                .filter(type -> type.valueType.equals(fieldType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        Object result = parser.biFunction.apply(resultSet, name);
        if (result == null) {
            return parser.defaultValue;
        }
        return result;

    }

    private static String getString(ResultSet resultSet, String name) {
        try {
            return resultSet.getString(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private static int getInt(ResultSet resultSet, String name) {
        try {
            return resultSet.getInt(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private static long getLong(ResultSet resultSet, String name) {
        try {
            return resultSet.getLong(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private static boolean getBoolean(ResultSet resultSet, String name) {
        try {
            return resultSet.getBoolean(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private static byte getByte(ResultSet resultSet, String name) {
        try {
            return resultSet.getByte(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private static short getShort(ResultSet resultSet, String name) {
        try {
            return resultSet.getShort(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private static double getDouble(ResultSet resultSet, String name) {
        try {
            return resultSet.getDouble(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private static float getFloat(ResultSet resultSet, String name) {
        try {
            return resultSet.getFloat(name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}