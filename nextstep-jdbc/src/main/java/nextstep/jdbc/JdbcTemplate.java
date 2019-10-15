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
            throw new IllegalExecutionException("잘못된 Connection");
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
                | NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException("잘못된 Connection");
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
                | NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException("잘못된 Connection");
        }
    }

    private <T> T getObject(Class<?> clazz, ResultSet resultSet) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        Object object;
        Constructor constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        object = constructor.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(object, resultSet.getString(field.getName()));
        }
        return (T) object;
    }
}
