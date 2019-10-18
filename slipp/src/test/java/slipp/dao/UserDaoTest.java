package slipp.dao;

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

public class UserDaoTest {
    @BeforeEach
    public void setup() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() {
        final User expected = new User("userId", "password", "name", "javajigi@email.com");
        final UserDao userDao = new UserDao(ConnectionManager.getDataSource());

        userDao.create(expected);
        User actual = userDao.findByUserId(expected.getUserId()).get();
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId()).get();
        assertThat(actual).isEqualTo(expected);

        userDao.deleteByUserId(expected.getUserId());
        actual = userDao.findByUserId(expected.getUserId()).orElse(null);
        assertThat(actual).isNull();
    }

    @Test
    public void findAll() {
        final UserDao userDao = new UserDao(ConnectionManager.getDataSource());
        final List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}