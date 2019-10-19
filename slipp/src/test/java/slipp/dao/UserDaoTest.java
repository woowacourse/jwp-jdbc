package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDaoTest {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    private final UserDao userDao = new UserDao(jdbcTemplate);
    private User expected;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        expected = new User("userId", "password", "name", "ddu0422@naver.com");
        userDao.insert(expected);
    }

    @Test
    @DisplayName("사용자 조회")
    public void findAll() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("사용자 조회")
    void createAndRead() {
        User actual = userDao.findByUserId(expected.getUserId());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("사용자 변경")
    void update() {
        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);

        User actual = userDao.findByUserId(expected.getUserId());

        assertEquals(expected, actual);
    }

    @AfterEach
    @DisplayName("사용자 삭제")
    void remove() {
        assertThat(userDao.findAll()).hasSize(2);
        userDao.remove(expected.getUserId());
        assertThat(userDao.findAll()).hasSize(1);
    }
}