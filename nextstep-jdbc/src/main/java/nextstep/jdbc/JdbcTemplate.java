package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final String ILLEGAL_EXECUTION_EXCEPTION_MESSAGE = "잘못된 Connection";
    private static final String INSTANTIATION_FAILED_EXCEPTION_MESSAGE = "인스턴스 생성 실패";

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(String sql, String... values) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalConnectionException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException(ILLEGAL_EXECUTION_EXCEPTION_MESSAGE);
        }
    }

    public <T> T executeQuery(String sql, Class<?> clazz, String... values) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            T object = null;
            if (resultSet.next()) {
                object = getObject(clazz, resultSet);
            }

            return object;
        } catch (SQLException
                | IllegalConnectionException
                | IllegalAccessException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException(ILLEGAL_EXECUTION_EXCEPTION_MESSAGE);
        }
    }

    public <T> List<T> executeQuery(String sql, Class<?> clazz) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<T> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(getObject(clazz, resultSet));
            }

            return data;
        } catch (SQLException
                | IllegalConnectionException
                | IllegalAccessException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException(ILLEGAL_EXECUTION_EXCEPTION_MESSAGE);
        }
    }

    private <T> T getObject(Class<?> clazz, ResultSet resultSet) throws IllegalAccessException, SQLException {
        Object object = instantiate(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(object, resultSet.getString(field.getName()));
        }
        return (T) object;
    }

    private Object instantiate(Class<?> clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            log.error("instantiate error : ", e);
            throw new InstantiationFailedException(INSTANTIATION_FAILED_EXCEPTION_MESSAGE);
        }
    }
}
