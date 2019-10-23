package slipp.mapper;

import nextstep.jdbc.template.RowMapper;
import slipp.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("userId");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        return new User(id, password, name, email);
    }
}
