package nextstep.jdbc;

import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    public void executeSql(String sql, Object... arg) {

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < arg.length; i++) {
                preparedStatement.setObject(i + 1, arg[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}