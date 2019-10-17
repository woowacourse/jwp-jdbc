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
    private static final String EXSISTED_USER_ID = "admin";
    private final UserDao userDao = UserDao.getInstance();

    @BeforeEach
    void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void find() {
        assertThat(userDao.findByUserId(EXSISTED_USER_ID)).isNotNull();
//        assertThat(userDao.findByUserId(EXSISTED_USER_ID)).isNotEmpty();
    }

    @Test
    void find_fail() {
        assertThat(userDao.findByUserId("admin2")).isNull();
//        assertThat(userDao.findByUserId("admin2")).isEmpty();
    }

    @Test
    void create() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(expected);

        assertThat(userDao.findByUserId(expected.getUserId())).isEqualTo(expected);
    }

    @Test
    void update() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        assertThat(userDao.findByUserId(expected.getUserId())).isEqualTo(expected);
    }

    @Test
    void findAll() throws Exception {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}