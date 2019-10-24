package slipp.dao;

import nextstep.jdbc.ConnectionGenerator;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.jdbc.exception.SelectQueryException;
import nextstep.jdbc.exception.SqlUpdateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;
import slipp.exception.UserNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static slipp.dao.UserDao.DB_DRIVER;
import static slipp.dao.UserDao.DB_PW;
import static slipp.dao.UserDao.DB_URL;
import static slipp.dao.UserDao.DB_USERNAME;

public class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionGenerator.getDataSource(DB_DRIVER, DB_URL, DB_USERNAME, DB_PW));
        userDao = new UserDao();
    }

    @Test
    public void crud() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");

        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void update_exception() {
        User expected = new User(null, null, null, null);
        assertThrows(SqlUpdateException.class, () -> userDao.insert(expected));
    }

    @Test
    void select_exception() {
        assertThrows(UserNotFoundException.class, () -> userDao.findByUserId("userId"));
    }

    @Test
    void list_select_exception() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionGenerator.getDataSource(DB_DRIVER, DB_URL, DB_USERNAME, DB_PW));
        User user = new User("userId", "password", "name", "javajigi@email.com");
        RowMapper<User> rowMapper = rs -> user;
        assertThrows(SelectQueryException.class, () -> jdbcTemplate.listQuery(null, rowMapper));
    }

    @Test
    public void findAll() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}