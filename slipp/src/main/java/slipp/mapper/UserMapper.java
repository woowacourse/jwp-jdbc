package slipp.mapper;

import nextstep.jdbc.mapper.RowMapper;
import slipp.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {

    @Override
    public User create(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email"));
    }
}
