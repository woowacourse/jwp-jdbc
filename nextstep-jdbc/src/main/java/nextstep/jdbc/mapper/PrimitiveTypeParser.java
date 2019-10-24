package nextstep.jdbc.mapper;

import nextstep.jdbc.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PrimitiveTypeParser {
    SHORT(List.of(Short.class, short.class), ResultSet::getShort),
    INTEGER(List.of(Integer.class, int.class), ResultSet::getInt),
    LONG(List.of(Long.class, long.class), ResultSet::getLong),
    BOOLEAN(List.of(Boolean.class, boolean.class), ResultSet::getBoolean),
    FLOAT(List.of(Float.class, float.class), ResultSet::getFloat),
    DOUBLE(List.of(Double.class, double.class), ResultSet::getDouble),
    STRING(List.of(String.class), ResultSet::getString);

    private static final Logger logger = LoggerFactory.getLogger(PrimitiveTypeParser.class);

    public static Map<Class<?>, Parser> map = new HashMap<>();

    static {
        for (final PrimitiveTypeParser primitiveTypeParser : PrimitiveTypeParser.values()) {
            for (final Class<?> clazz : primitiveTypeParser.type) {
                map.put(clazz, primitiveTypeParser.parser);
            }
        }
    }

    private final List<Class<?>> type;
    private final Parser parser;

    PrimitiveTypeParser(final List<Class<?>> type, final Parser parser) {
        this.type = type;
        this.parser = parser;
    }

    public static <T> T map(final ResultSet resultSet, final String name, final Class<T> type) {
        @SuppressWarnings("unckecked") // type을 확인하고 사용하기 때문에 안전성이 보장된다.
        final T template = (T) map.get(type).template(resultSet, name);
        return template;
    }

    public static boolean isMapping(final Class<?> type) {
        return map.containsKey(type);
    }

    @FunctionalInterface
    public interface Parser {
        default Object template(final ResultSet rs, final String name) {
            try {
                return parse(rs, name);
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new DataAccessException(e);
            }
        }

        Object parse(final ResultSet rs, final String name) throws SQLException;
    }
}
