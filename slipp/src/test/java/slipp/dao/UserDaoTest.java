package slipp.dao;

import nextstep.jdbc.ConnectionGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static slipp.dao.UserDao.DB_DRIVER;
import static slipp.dao.UserDao.DB_PW;
import static slipp.dao.UserDao.DB_URL;
import static slipp.dao.UserDao.DB_USERNAME;

public class UserDaoTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionGenerator.getDataSource(DB_DRIVER, DB_URL, DB_USERNAME, DB_PW));
    }

    @Test
    public void crud() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() {
        UserDao userDao = new UserDao();
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}