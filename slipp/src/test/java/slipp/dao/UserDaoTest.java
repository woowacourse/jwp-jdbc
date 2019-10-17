package slipp.dao;

import nextstep.jdbc.JdbcTemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTest {
    private UserDao userDao;
    private User expected;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        userDao = UserDao.getInstance();
        expected = new User("userId", "password", "name", "javajigi@email.com");
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() {
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void create_fail() {
        userDao.insert(expected);
        assertThrows(JdbcTemplateException.class, () -> userDao.insert(expected));
    }

    @Test
    void read_fail() {
        assertNull(userDao.findByUserId("NO_USER"));
    }

    @Test
    public void findAll() {
        UserDao userDao = UserDao.getInstance();
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}