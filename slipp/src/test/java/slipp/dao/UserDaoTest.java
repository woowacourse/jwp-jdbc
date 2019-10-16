package slipp.dao;

import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;
import slipp.exception.UserNotFoundException;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTest {

    private UserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

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
    public void findAll() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    @DisplayName("중복된 User를 저장하면 예외가 발생한다")
    void addDuplicateUser() {
        User user = new User("userId", "password", "name", "javajigi@email.com");

        userDao.insert(user);
        assertThrows(ExecuteUpdateFailedException.class, () -> userDao.insert(user));
    }

    @Test
    @DisplayName("없는 User를 조회하면 예외가 발생한다")
    void findByUserId_notFound() {
        assertThrows(UserNotFoundException.class, () -> userDao.findByUserId("userId"));
    }
}