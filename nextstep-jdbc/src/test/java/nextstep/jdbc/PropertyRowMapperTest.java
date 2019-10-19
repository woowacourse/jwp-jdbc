package nextstep.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PropertyRowMapperTest {
    private PropertyRowMapper<User> rowMapper = PropertyRowMapper.from(User.class);

    @Test
    void mapRowTest() throws SQLException {
        // given
        final User expected = new User("id", "password", "name", 12);
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getString("userId")).thenReturn(expected.getUserId());
        when(rs.getInt("age")).thenReturn(expected.getAge());
        when(rs.getString("name")).thenReturn(expected.getName());
        when(rs.getString("password")).thenReturn(expected.getPassword());

        // when
        final User actual = rowMapper.mapRow(rs);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}