package nextstep.jdbc.utils;

import nextstep.jdbc.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

// todo 예외처리 때문에 람다 사용하면 코드가 더러워져서 메소드를 만들어줬지만, 예외처리를 위한 중복코드가 생기는 문제
public enum TypeParser {
    SHORT(List.of(Short.class, short.class), TypeParser::getShort),
    INTEGER(List.of(Integer.class, int.class), TypeParser::getInteger),
    LONG(List.of(Long.class, long.class), TypeParser::getLong),
    BOOLEAN(List.of(Boolean.class, boolean.class), TypeParser::getBoolean),
    FLOAT(List.of(Float.class, float.class), TypeParser::getFloat),
    DOUBLE(List.of(Double.class, double.class), TypeParser::getDouble),
    STRING(List.of(String.class), TypeParser::getString);

    private static final Logger logger = LoggerFactory.getLogger(TypeParser.class);

    public static Map<Class<?>, BiFunction<ResultSet, String, Object>> map = new HashMap<>();

    static {
        for (final TypeParser typeParser : TypeParser.values()) {
            for (final Class<?> clazz : typeParser.type) {
                map.put(clazz, typeParser.parser);
            }
        }
    }

    private final List<Class<?>> type;
    private final BiFunction<ResultSet, String, Object> parser;

    TypeParser(final List<Class<?>> type, final BiFunction<ResultSet, String, Object> parser) {
        this.type = type;
        this.parser = parser;
    }

    public static Object parse(final ResultSet resultSet, final String name, final Class<?> type) {
        return map.get(type).apply(resultSet, name);
    }

    private static String getString(final ResultSet resultSet, final String name) {
        try {
            return resultSet.getString(name);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private static int getInteger(final ResultSet resultSet, final String name) {
        try {
            return resultSet.getInt(name);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private static long getLong(final ResultSet resultSet, final String name) {
        try {
            return resultSet.getLong(name);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private static boolean getBoolean(final ResultSet resultSet, final String name) {
        try {
            return resultSet.getBoolean(name);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private static short getShort(final ResultSet resultSet, final String name) {
        try {
            return resultSet.getShort(name);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private static double getDouble(final ResultSet resultSet, final String name) {
        try {
            return resultSet.getDouble(name);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private static float getFloat(final ResultSet resultSet, final String name) {
        try {
            return resultSet.getFloat(name);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DataAccessException(e);
        }
    }
}