package nextstep.jdbc;

import nextstep.jdbc.query.SqlMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(SqlMapperTest.class);

    @Test
    void create() {
        User user = new User("aiden", "1234", "aiden", "aiden@naver.com");
        SqlMapper sqlMapper = new SqlMapper("INSERT INTO USERS VALUES (?, ?, ?, ?)");
        sqlMapper.addAttribute(user.getUserId())
                .addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail());

        SqlMapper expected = new SqlMapper("INSERT INTO USERS VALUES (?, ?, ?, ?)");
        expected.addAttribute(user.getUserId())
                .addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail());

        assertThat(sqlMapper).isEqualTo(expected);
    }

    // TODO: execute test
}